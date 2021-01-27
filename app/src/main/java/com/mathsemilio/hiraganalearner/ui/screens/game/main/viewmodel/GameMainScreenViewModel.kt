package com.mathsemilio.hiraganalearner.ui.screens.game.main.viewmodel

import com.mathsemilio.hiraganalearner.common.BaseObservable
import com.mathsemilio.hiraganalearner.domain.hiragana.HiraganaSymbol
import com.mathsemilio.hiraganalearner.game.backend.BackendEventListener
import com.mathsemilio.hiraganalearner.game.backend.GameBackend
import com.mathsemilio.hiraganalearner.game.backend.ViewModelRequestEventListener

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

    fun onClearInstance() {
        mGameBackend.removeListener(this)
    }

    override fun onSymbolUpdated(newSymbol: HiraganaSymbol) {
        mCurrentHiraganaSymbol = newSymbol
        getListeners().forEach { it.onCurrentHiraganaSymbolUpdated(newSymbol) }
    }

    override fun onGameScoreUpdated(newScore: Int) {
        mCurrentGameScore = newScore
        getListeners().forEach { it.onGameScoreUpdated(newScore) }
    }

    override fun onGameProgressUpdated(updatedProgress: Int) {
        getListeners().forEach { it.onGameProgressUpdated(updatedProgress) }
    }

    override fun onGameCountdownTimeUpdated(updatedCountdownTime: Int) {
        getListeners().forEach { it.onGameCountDownTimeUpdated(updatedCountdownTime) }
    }

    override fun onRomanizationGroupUpdated(updatedRomanizationGroupList: List<String>) {
        getListeners().forEach { it.onRomanizationGroupUpdated(updatedRomanizationGroupList) }
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