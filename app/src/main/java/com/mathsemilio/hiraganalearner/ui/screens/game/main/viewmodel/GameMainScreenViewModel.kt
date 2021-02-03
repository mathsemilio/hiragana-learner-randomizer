package com.mathsemilio.hiraganalearner.ui.screens.game.main.viewmodel

import com.mathsemilio.hiraganalearner.common.observable.BaseObservable
import com.mathsemilio.hiraganalearner.domain.hiragana.HiraganaSymbol
import com.mathsemilio.hiraganalearner.game.backend.GameBackend

class GameMainScreenViewModel : BaseObservable<GameMainScreenViewModel.Listener>(), GameBackend.Listener {

    interface Listener {
        fun onGameScoreUpdated(newScore: Int)
        fun onGameProgressUpdated(updatedProgress: Int)
        fun onGameCountDownTimeUpdated(updatedCountdownTime: Int)
        fun onRomanizationGroupUpdated(updatedRomanizationGroupList: List<String>)
        fun onCurrentHiraganaSymbolUpdated(newSymbol: HiraganaSymbol)
        fun onCorrectAnswer()
        fun onWrongAnswer()
        fun onGameTimeOver()
    }

    private val gameBackend = GameBackend()
    private val viewModelRequest = gameBackend as ViewModelRequestEventListener

    private lateinit var _currentHiraganaSymbol: HiraganaSymbol
    val currentHiraganaSymbol get() = _currentHiraganaSymbol

    private var _currentGameScore = 0
    val currentGameScore get() = _currentGameScore

    var gameFinished = false

    init {
        gameBackend.addListener(this)
    }

    fun startGame(difficultyValue: Int) {
        viewModelRequest.onStartGameRequested(difficultyValue)
    }

    fun checkUserAnswer(selectedRomanization: String) {
        viewModelRequest.onCheckUserAnswerRequested(selectedRomanization)
    }

    fun getNextSymbol() {
        viewModelRequest.onGetNextSymbolRequested()
    }

    fun pauseGameTimer() {
        viewModelRequest.onPauseGameTimerRequested()
    }

    fun resumeGameTimer() {
        viewModelRequest.onResumeGameTimerRequested()
    }

    fun onClearInstance() {
        gameBackend.removeListener(this)
    }

    override fun onSymbolUpdated(newSymbol: HiraganaSymbol) {
        _currentHiraganaSymbol = newSymbol
        getListeners().forEach { it.onCurrentHiraganaSymbolUpdated(newSymbol) }
    }

    override fun onGameScoreUpdated(newScore: Int) {
        _currentGameScore = newScore
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