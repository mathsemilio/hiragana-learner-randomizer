/*
Copyright 2020 Matheus Menezes

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.mathsemilio.hiraganalearner.domain.backend

import android.os.CountDownTimer
import com.mathsemilio.hiraganalearner.common.*
import com.mathsemilio.hiraganalearner.common.observable.BaseObservable
import com.mathsemilio.hiraganalearner.domain.model.HiraganaSymbol
import com.mathsemilio.hiraganalearner.others.hiraganaSymbolsList
import kotlin.random.Random

class GameBackend : BaseObservable<GameBackend.Listener>(), BackendMediatorRequestListener {

    interface Listener {
        fun onSymbolUpdated(symbol: HiraganaSymbol)

        fun onScoreUpdated(score: Int)

        fun onProgressUpdated(progress: Int)

        fun onCountdownTimeUpdated(countdownTime: Int)

        fun onRomanizationOptionsUpdated(romanizations: List<String>)

        fun onCorrectAnswer()

        fun onWrongAnswer()

        fun onGameTimeOver()

        fun onGameFinished()
    }

    private lateinit var countDownTimer: CountDownTimer

    private val hiraganaSymbols = hiraganaSymbolsList.toMutableList()

    private var totalCountdownTime = 0L
    private var currentCountdownTime = 0L
    private var currentScore = 0
    private var currentProgress = 0

    private lateinit var firstRomanizationOption: String
    private lateinit var secondRomanizationOption: String
    private lateinit var thirdRomanizationOption: String
    private lateinit var fourthRomanizationOption: String

    private fun startGame(difficultyValue: Int) {
        totalCountdownTime = getTotalCountdownTime(difficultyValue)

        hiraganaSymbols.shuffle()

        notifyGameScoreUpdated(currentScore)
        notifySymbolUpdated(hiraganaSymbols.first())
        generateRomanizationOptions()
        startCountdownTimer(totalCountdownTime)
    }

    private fun getTotalCountdownTime(difficultyValue: Int): Long {
        return when (difficultyValue) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> COUNTDOWN_TIME_BEGINNER
            GAME_DIFFICULTY_VALUE_MEDIUM -> COUNTDOWN_TIME_MEDIUM
            GAME_DIFFICULTY_VALUE_HARD -> COUNTDOWN_TIME_HARD
            else -> throw IllegalArgumentException(ILLEGAL_GAME_DIFFICULTY_VALUE)
        }
    }

    private fun startCountdownTimer(totalCountdownTime: Long) {
        countDownTimer = object : CountDownTimer(totalCountdownTime, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                currentCountdownTime = (millisUntilFinished / 1000).also {
                    notifyCountdownTimeUpdated(it.toInt())
                }
            }

            override fun onFinish() {
                currentCountdownTime = 0L
                notifyTimeOver()
            }
        }.start()
    }

    private fun pauseTimer() = countDownTimer.cancel()

    private fun resumeTimer() = startCountdownTimer(currentCountdownTime.times(1000L))

    private fun checkAnswer(selectedRomanization: String) {
        pauseTimer()

        if (hiraganaSymbols.first().romanization == selectedRomanization) {
            notifyGameScoreUpdated(++currentScore)
            notifyCorrectAnswer()
        } else {
            notifyWrongAnswer()
        }
    }

    private fun getNextSymbol() {
        notifyProgressUpdated(++currentProgress)
        hiraganaSymbols.removeAt(0)
        notifySymbolUpdated(hiraganaSymbols.first())

        generateRomanizationOptions()
        startCountdownTimer(totalCountdownTime)

        if (hiraganaSymbols.size == 1) notifyGameFinished()
    }

    private fun generateRomanizationOptions() {
        val romanizationList = arrayOf(
            "A", "I", "U", "E", "O", "KA", "KI", "KU", "KE", "KO", "SA", "SHI", "SU", "SE", "SO",
            "TA", "CHI", "TSU", "TE", "TO", "NA", "NI", "NU", "NE", "NO", "HA", "HI", "FU", "HE",
            "HO", "MA", "MI", "MU", "ME", "MO", "YA", "YU", "YO", "RA", "RI", "RU", "RE", "RO",
            "WA", "WO", "N"
        ).let { romanizationList ->
            romanizationList.shuffle()
            romanizationList.filterNot { romanization ->
                romanization == hiraganaSymbols.first().romanization
            }
        }

        firstRomanizationOption = romanizationList.slice(0..11).random()
        secondRomanizationOption = romanizationList.slice(12..23).random()
        thirdRomanizationOption = romanizationList.slice(24..35).random()
        fourthRomanizationOption = romanizationList.slice(36..44).random()

        setCorrectRomanizationAnswer()

        onRomanizationGroupUpdated(
            listOf(
                firstRomanizationOption,
                secondRomanizationOption,
                thirdRomanizationOption,
                fourthRomanizationOption
            )
        )
    }

    private fun setCorrectRomanizationAnswer() {
        when (Random.nextInt(4)) {
            0 -> firstRomanizationOption = hiraganaSymbols.first().romanization
            1 -> secondRomanizationOption = hiraganaSymbols.first().romanization
            2 -> thirdRomanizationOption = hiraganaSymbols.first().romanization
            3 -> fourthRomanizationOption = hiraganaSymbols.first().romanization
        }
    }

    override fun onStartGameRequested(difficultyValue: Int) {
        startGame(difficultyValue)
    }

    override fun onCheckAnswerRequested(selectedRomanization: String) {
        checkAnswer(selectedRomanization)
    }

    override fun onGetNextSymbolRequested() {
        getNextSymbol()
    }

    override fun onPauseTimerRequested() {
        pauseTimer()
    }

    override fun onResumeGameRequested() {
        resumeTimer()
    }

    private fun notifySymbolUpdated(symbol: HiraganaSymbol) {
        listeners.forEach { listener ->
            listener.onSymbolUpdated(symbol)
        }
    }

    private fun notifyCountdownTimeUpdated(countDownTime: Int) {
        listeners.forEach { listener ->
            listener.onCountdownTimeUpdated(countDownTime)
        }
    }

    private fun notifyCorrectAnswer() {
        listeners.forEach { listener ->
            listener.onCorrectAnswer()
        }
    }

    private fun notifyWrongAnswer() {
        listeners.forEach { listener ->
            listener.onWrongAnswer()
        }
    }

    private fun notifyTimeOver() {
        listeners.forEach { listener ->
            listener.onGameTimeOver()
        }
    }

    private fun notifyGameFinished() {
        listeners.forEach { listener ->
            listener.onGameFinished()
        }
    }

    private fun notifyGameScoreUpdated(score: Int) {
        listeners.forEach { listener ->
            listener.onScoreUpdated(score)
        }
    }

    private fun notifyProgressUpdated(progress: Int) {
        listeners.forEach { listener ->
            listener.onProgressUpdated(progress)
        }
    }

    private fun onRomanizationGroupUpdated(romanizationGroupList: List<String>) {
        listeners.forEach { listener ->
            listener.onRomanizationOptionsUpdated(romanizationGroupList)
        }
    }
}