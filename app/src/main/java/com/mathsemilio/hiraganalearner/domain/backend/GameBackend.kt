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
import com.mathsemilio.hiraganalearner.domain.model.SyllabarySymbol
import com.mathsemilio.hiraganalearner.others.syllabarySymbolsList
import kotlin.random.Random

class GameBackend : BaseObservable<GameBackend.Listener>(), BackendMediatorRequestListener {

    interface Listener {
        fun onSymbolUpdated(symbol: SyllabarySymbol)

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

    private val syllabarySymbols = syllabarySymbolsList.toMutableList()

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

        syllabarySymbols.shuffle()

        notifyGameScoreUpdated(currentScore)
        notifySymbolUpdated(syllabarySymbols.first())
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
                currentCountdownTime = (millisUntilFinished / 1000).also { countdownTime ->
                    notifyCountdownTimeUpdated(countdownTime.toInt())
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

        if (syllabarySymbols.first().romanization == selectedRomanization) {
            notifyGameScoreUpdated(++currentScore)
            notifyCorrectAnswer()
        } else {
            notifyWrongAnswer()
        }
    }

    private fun getNextSymbol() {
        notifyProgressUpdated(++currentProgress)
        syllabarySymbols.removeAt(0)
        notifySymbolUpdated(syllabarySymbols.first())

        generateRomanizationOptions()
        startCountdownTimer(totalCountdownTime)

        if (syllabarySymbols.size == 1) notifyGameFinished()
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
                romanization == syllabarySymbols.first().romanization
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
            0 -> firstRomanizationOption = syllabarySymbols.first().romanization
            1 -> secondRomanizationOption = syllabarySymbols.first().romanization
            2 -> thirdRomanizationOption = syllabarySymbols.first().romanization
            3 -> fourthRomanizationOption = syllabarySymbols.first().romanization
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

    private fun notifySymbolUpdated(symbol: SyllabarySymbol) {
        notifyListener { listener ->
            listener.onSymbolUpdated(symbol)
        }
    }

    private fun notifyCountdownTimeUpdated(countDownTime: Int) {
        notifyListener { listener ->
            listener.onCountdownTimeUpdated(countDownTime)
        }
    }

    private fun notifyCorrectAnswer() {
        notifyListener { listener ->
            listener.onCorrectAnswer()
        }
    }

    private fun notifyWrongAnswer() {
        notifyListener { listener ->
            listener.onWrongAnswer()
        }
    }

    private fun notifyTimeOver() {
        notifyListener { listener ->
            listener.onGameTimeOver()
        }
    }

    private fun notifyGameFinished() {
        notifyListener { listener ->
            listener.onGameFinished()
        }
    }

    private fun notifyGameScoreUpdated(score: Int) {
        notifyListener { listener ->
            listener.onScoreUpdated(score)
        }
    }

    private fun notifyProgressUpdated(progress: Int) {
        notifyListener { listener ->
            listener.onProgressUpdated(progress)
        }
    }

    private fun onRomanizationGroupUpdated(romanizationGroupList: List<String>) {
        notifyListener { listener ->
            listener.onRomanizationOptionsUpdated(romanizationGroupList)
        }
    }
}