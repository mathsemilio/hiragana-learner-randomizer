package com.mathsemilio.hiraganalearner.game.backend

import android.os.CountDownTimer
import com.mathsemilio.hiraganalearner.common.*
import com.mathsemilio.hiraganalearner.domain.hiragana.HiraganaSymbol
import com.mathsemilio.hiraganalearner.others.hiraganaSymbolsList
import com.mathsemilio.hiraganalearner.ui.screens.common.BaseObservable
import kotlin.random.Random

class GameBackend : BaseObservable<BackendEventListener>(), ViewModelRequestEventListener {

    private lateinit var mCountDownTimer: CountDownTimer

    private val mHiraganaSymbolList = hiraganaSymbolsList.toMutableList()

    private var mDifficultyCountDownTime = 0L
    private var mCurrentCountDownTime = 0L
    private var mScore = 0
    private var mProgress = 0

    private var mFirstRomanizationGroupString = ""
    private var mSecondRomanizationGroupString = ""
    private var mThirdRomanizationGroupString = ""
    private var mFourthRomanizationGroupString = ""

    private fun startGame(difficultyValue: Int) {
        mDifficultyCountDownTime = getCountDownTimeBasedOnDifficultyValue(difficultyValue)

        mHiraganaSymbolList.shuffle()

        onGameScoreUpdated(mScore)
        onSymbolUpdated(mHiraganaSymbolList.first())
        generateRomanizationGroup()
        startTimer(mDifficultyCountDownTime)
    }

    private fun getCountDownTimeBasedOnDifficultyValue(difficultyValue: Int): Long {
        return when (difficultyValue) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> COUNTDOWN_TIME_BEGINNER
            GAME_DIFFICULTY_VALUE_MEDIUM -> COUNTDOWN_TIME_MEDIUM
            GAME_DIFFICULTY_VALUE_HARD -> COUNTDOWN_TIME_HARD
            else -> throw IllegalArgumentException(ILLEGAL_GAME_DIFFICULTY_VALUE)
        }
    }

    private fun startTimer(difficultyCountDownTimer: Long) {
        mCountDownTimer = object : CountDownTimer(difficultyCountDownTimer, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                mCurrentCountDownTime = (millisUntilFinished / 1000).also {
                    onCountDownTimerUpdated(it.toInt())
                }
            }

            override fun onFinish() {
                mCurrentCountDownTime = 0L
                onGameTimeOver()
            }
        }
        mCountDownTimer.start()
    }

    private fun cancelTimer() = mCountDownTimer.cancel()

    private fun resumeTimer() = startTimer(mCurrentCountDownTime.times(ONE_SECOND))

    private fun checkAnswer(selectedRomanization: String) {
        cancelTimer()

        if (mHiraganaSymbolList.first().romanization == selectedRomanization) {
            onGameScoreUpdated(++mScore)
            onCorrectAnswer()
        } else
            onWrongAnswer()
    }

    private fun getNextSymbol() {
        onGameProgressUpdated(++mProgress)
        mHiraganaSymbolList.removeAt(0)
        onSymbolUpdated(mHiraganaSymbolList.first())

        if (mHiraganaSymbolList.size == 1) {
            generateRomanizationGroup()
            startTimer(mDifficultyCountDownTime)
            onGameFinished()
        } else {
            generateRomanizationGroup()
            startTimer(mDifficultyCountDownTime)
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
            romanizationList.filterNot { it == mHiraganaSymbolList.first().romanization }
        }

        mFirstRomanizationGroupString = romanizationList.slice(0..11).random()
        mSecondRomanizationGroupString = romanizationList.slice(12..23).random()
        mThirdRomanizationGroupString = romanizationList.slice(24..35).random()
        mFourthRomanizationGroupString = romanizationList.slice(36..44).random()

        setCorrectRomanizationAnswer()

        onRomanizationGroupUpdated(
            listOf(
                mFirstRomanizationGroupString,
                mSecondRomanizationGroupString,
                mThirdRomanizationGroupString,
                mFourthRomanizationGroupString
            )
        )
    }

    private fun setCorrectRomanizationAnswer() {
        when (Random.nextInt(4)) {
            0 -> mFirstRomanizationGroupString = mHiraganaSymbolList.first().romanization
            1 -> mSecondRomanizationGroupString = mHiraganaSymbolList.first().romanization
            2 -> mThirdRomanizationGroupString = mHiraganaSymbolList.first().romanization
            3 -> mFourthRomanizationGroupString = mHiraganaSymbolList.first().romanization
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
        cancelTimer()
    }

    override fun onResumeGameTimerRequested() {
        resumeTimer()
    }

    private fun onSymbolUpdated(symbol: HiraganaSymbol) =
        getListeners().forEach { it.onSymbolUpdated(symbol) }

    private fun onCountDownTimerUpdated(countDownTime: Int) =
        getListeners().forEach { it.onGameCountdownTimeUpdated(countDownTime) }

    private fun onCorrectAnswer() =
        getListeners().forEach { it.onCorrectAnswer() }

    private fun onWrongAnswer() =
        getListeners().forEach { it.onWrongAnswer() }

    private fun onGameTimeOver() =
        getListeners().forEach { it.onGameTimeOver() }

    private fun onGameFinished() =
        getListeners().forEach { it.onGameFinished() }

    private fun onGameScoreUpdated(score: Int) =
        getListeners().forEach { it.onGameScoreUpdated(score) }

    private fun onGameProgressUpdated(progress: Int) =
        getListeners().forEach { it.onGameProgressUpdated(progress) }

    private fun onRomanizationGroupUpdated(romanizationGroupList: List<String>) =
        getListeners().forEach { it.onRomanizationGroupUpdated(romanizationGroupList) }
}