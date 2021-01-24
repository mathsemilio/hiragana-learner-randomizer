package com.mathsemilio.hiraganalearner.ui.screens.game.main.viewmodel

import com.mathsemilio.hiraganalearner.domain.hiragana.HiraganaSymbol
import com.mathsemilio.hiraganalearner.game.backend.BackendEventListener
import com.mathsemilio.hiraganalearner.game.backend.GameBackend
import com.mathsemilio.hiraganalearner.game.backend.ViewModelRequestEventListener
import com.mathsemilio.hiraganalearner.ui.screens.common.BaseObservable

class GameMainScreenViewModel : BaseObservable<ViewModelEventListener>(), BackendEventListener {

    private val mGameBackend = GameBackend()
    private val mViewModelRequest = mGameBackend as ViewModelRequestEventListener

    private lateinit var mCurrentHiraganaSymbol: HiraganaSymbol
    private var mCurrentGameScore = 0
    var gameFinished = false

    init {
        mGameBackend.registerListener(this)
    }

    fun startGame(difficultyValue: Int) {
        mViewModelRequest.onStartGameRequested(difficultyValue)
    }

    fun checkUserAnswer(selectedRomanization: String) {
        mViewModelRequest.onCheckUserAnswerRequested(selectedRomanization)
    }

    fun getNextSymbol() {
        mViewModelRequest.onGetNextSymbolRequested()
    }

    fun pauseGameTimer() {
        mViewModelRequest.onPauseGameTimerRequested()
    }

    fun resumeGameTimer() {
        mViewModelRequest.onResumeGameTimerRequested()
    }

    fun getCurrentSymbol(): HiraganaSymbol {
        return mCurrentHiraganaSymbol
    }

    fun getGameScore(): Int {
        return mCurrentGameScore
    }

    fun onControllerOnDestroyView() {
        mGameBackend.removeListener(this)
    }

    override fun onSymbolUpdated(symbol: HiraganaSymbol) {
        mCurrentHiraganaSymbol = symbol
        getListeners().forEach { it.onCurrentHiraganaSymbolUpdated(symbol) }
    }

    override fun onGameScoreUpdated(score: Int) {
        mCurrentGameScore = score
        getListeners().forEach { it.onGameScoreUpdated(score) }
    }

    override fun onGameProgressUpdated(progress: Int) {
        getListeners().forEach { it.onGameProgressUpdated(progress) }
    }

    override fun onGameCountdownTimeUpdated(countDownTime: Int) {
        getListeners().forEach { it.onGameCountDownTimeUpdated(countDownTime) }
    }

    override fun onRomanizationGroupUpdated(romanizationGroupList: List<String>) {
        getListeners().forEach { it.onRomanizationGroupUpdated(romanizationGroupList) }
    }

    override fun onCorrectAnswer() {
        getListeners().forEach { it.onCorrectAnswer() }
    }

    override fun onWrongAnswer() {
        getListeners().forEach { it.onWrongAnswer() }
    }

    override fun onGameTimeOver() {
        getListeners().forEach { it.onGameTimeOver() }
    }

    override fun onGameFinished() {
        gameFinished = true
    }
}