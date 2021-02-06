package com.mathsemilio.hiraganalearner.game.backend

import android.os.CountDownTimer
import com.mathsemilio.hiraganalearner.common.*
import com.mathsemilio.hiraganalearner.common.observable.BaseObservable
import com.mathsemilio.hiraganalearner.domain.hiragana.HiraganaSymbol
import com.mathsemilio.hiraganalearner.others.hiraganaSymbolsList
import com.mathsemilio.hiraganalearner.game.model.ModelRequestEventListener
import kotlin.random.Random

class GameBackend : BaseObservable<GameBackend.Listener>(), ModelRequestEventListener {

    interface Listener {
        fun onSymbolUpdated(newSymbol: HiraganaSymbol)
        fun onGameScoreUpdated(newScore: Int)
        fun onGameProgressUpdated(updatedProgress: Int)
        fun onGameCountdownTimeUpdated(updatedCountdownTime: Int)
        fun onRomanizationGroupUpdated(updatedRomanizationGroupList: List<String>)
        fun onCorrectAnswer()
        fun onWrongAnswer()
        fun onGameTimeOver()
        fun onGameFinished()
    }

    private lateinit var countDownTimer: CountDownTimer

    private val hiraganaSymbolList = hiraganaSymbolsList.toMutableList()

    private var difficultyCountDownTime = 0L
    private var currentCountDownTime = 0L
    private var score = 0
    private var progress = 0

    private var firstRomanizationGroupString = ""
    private var secondRomanizationGroupString = ""
    private var thirdRomanizationGroupString = ""
    private var fourthRomanizationGroupString = ""

    private fun startGame(difficultyValue: Int) {
        difficultyCountDownTime = getCountdownTimeBasedOnDifficultyValue(difficultyValue)

        hiraganaSymbolList.shuffle()

        onGameScoreUpdated(score)
        onSymbolUpdated(hiraganaSymbolList.first())
        generateRomanizationGroup()
        startTimer(difficultyCountDownTime)
    }

    private fun getCountdownTimeBasedOnDifficultyValue(difficultyValue: Int): Long {
        return when (difficultyValue) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> COUNTDOWN_TIME_BEGINNER
            GAME_DIFFICULTY_VALUE_MEDIUM -> COUNTDOWN_TIME_MEDIUM
            GAME_DIFFICULTY_VALUE_HARD -> COUNTDOWN_TIME_HARD
            else -> throw IllegalArgumentException(ILLEGAL_GAME_DIFFICULTY_VALUE)
        }
    }

    private fun startTimer(difficultyCountdownTime: Long) {
        countDownTimer = object : CountDownTimer(difficultyCountdownTime, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                currentCountDownTime = (millisUntilFinished / 1000).also {
                    onCountDownTimeUpdated(it.toInt())
                }
            }

            override fun onFinish() {
                currentCountDownTime = 0L
                onGameTimeOver()
            }
        }
        countDownTimer.start()
    }

    private fun pauseTimer() = countDownTimer.cancel()

    private fun resumeTimer() = startTimer(currentCountDownTime.times(1000L))

    private fun checkAnswer(selectedRomanization: String) {
        pauseTimer()

        if (hiraganaSymbolList.first().romanization == selectedRomanization) {
            onGameScoreUpdated(++score)
            onCorrectAnswer()
        } else
            onWrongAnswer()
    }

    private fun getNextSymbol() {
        onGameProgressUpdated(++progress)
        hiraganaSymbolList.removeAt(0)
        onSymbolUpdated(hiraganaSymbolList.first())

        if (hiraganaSymbolList.size == 1) {
            generateRomanizationGroup()
            startTimer(difficultyCountDownTime)
            onGameFinished()
        } else {
            generateRomanizationGroup()
            startTimer(difficultyCountDownTime)
        }
    }

    private fun generateRomanizationGroup() {
        val romanizationList = arrayOf(
            "A", "I", "U", "E", "O", "KA", "KI", "KU", "KE", "KO", "SA", "SHI", "SU", "SE", "SO",
            "TA", "CHI", "TSU", "TE", "TO", "NA", "NI", "NU", "NE", "NO", "HA", "HI", "FU", "HE",
            "HO", "MA", "MI", "MU", "ME", "MO", "YA", "YU", "YO", "RA", "RI", "RU", "RE", "RO",
            "WA", "WO", "N"
        ).let { romanizationList ->
            romanizationList.shuffle()
            romanizationList.filterNot { it == hiraganaSymbolList.first().romanization }
        }

        firstRomanizationGroupString = romanizationList.slice(0..11).random()
        secondRomanizationGroupString = romanizationList.slice(12..23).random()
        thirdRomanizationGroupString = romanizationList.slice(24..35).random()
        fourthRomanizationGroupString = romanizationList.slice(36..44).random()

        setCorrectRomanizationAnswer()

        onRomanizationGroupUpdated(
            listOf(
                firstRomanizationGroupString,
                secondRomanizationGroupString,
                thirdRomanizationGroupString,
                fourthRomanizationGroupString
            )
        )
    }

    private fun setCorrectRomanizationAnswer() {
        when (Random.nextInt(4)) {
            0 -> firstRomanizationGroupString = hiraganaSymbolList.first().romanization
            1 -> secondRomanizationGroupString = hiraganaSymbolList.first().romanization
            2 -> thirdRomanizationGroupString = hiraganaSymbolList.first().romanization
            3 -> fourthRomanizationGroupString = hiraganaSymbolList.first().romanization
        }
    }

    override fun onStartGameRequested(difficultyValue: Int) {
        startGame(difficultyValue)
    }

    override fun onCheckUserAnswerRequested(selectedRomanization: String) {
        checkAnswer(selectedRomanization)
    }

    override fun onGetNextSymbolRequested() {
        getNextSymbol()
    }

    override fun onPauseGameTimerRequested() {
        pauseTimer()
    }

    override fun onResumeGameTimerRequested() {
        resumeTimer()
    }

    private fun onSymbolUpdated(symbol: HiraganaSymbol) {
        getListeners().forEach { it.onSymbolUpdated(symbol) }
    }

    private fun onCountDownTimeUpdated(countDownTime: Int) {
        getListeners().forEach { it.onGameCountdownTimeUpdated(countDownTime) }
    }

    private fun onCorrectAnswer() {
        getListeners().forEach { it.onCorrectAnswer() }
    }

    private fun onWrongAnswer() {
        getListeners().forEach { it.onWrongAnswer() }
    }

    private fun onGameTimeOver() {
        getListeners().forEach { it.onGameTimeOver() }
    }

    private fun onGameFinished() {
        getListeners().forEach { it.onGameFinished() }
    }

    private fun onGameScoreUpdated(score: Int) {
        getListeners().forEach { it.onGameScoreUpdated(score) }
    }

    private fun onGameProgressUpdated(progress: Int) {
        getListeners().forEach { it.onGameProgressUpdated(progress) }
    }

    private fun onRomanizationGroupUpdated(romanizationGroupList: List<String>) {
        getListeners().forEach { it.onRomanizationGroupUpdated(romanizationGroupList) }
    }
}