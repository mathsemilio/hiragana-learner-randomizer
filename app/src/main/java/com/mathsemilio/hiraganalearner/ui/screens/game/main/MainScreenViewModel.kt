package com.mathsemilio.hiraganalearner.ui.screens.game.main

import com.mathsemilio.hiraganalearner.common.observable.BaseObservable
import com.mathsemilio.hiraganalearner.domain.model.HiraganaSymbol
import com.mathsemilio.hiraganalearner.domain.backend.GameBackend

class MainScreenViewModel : BaseObservable<MainScreenViewModel.Listener>(), GameBackend.Listener {

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
    private val modelRequest = gameBackend as ViewModelRequestEventListener

    private lateinit var _symbol: HiraganaSymbol
    val symbol get() = _symbol

    private var _score = 0
    val score get() = _score

    private var _gameFinished = false
    val gameFinished get() = _gameFinished

    init {
        gameBackend.addListener(this)
    }

    fun startGame(difficultyValue: Int) {
        modelRequest.onStartGameRequested(difficultyValue)
    }

    fun checkUserAnswer(selectedRomanization: String) {
        modelRequest.onCheckUserAnswerRequested(selectedRomanization)
    }

    fun getNextSymbol() {
        modelRequest.onGetNextSymbolRequested()
    }

    fun pauseGameTimer() {
        modelRequest.onPauseGameTimerRequested()
    }

    fun resumeGameTimer() {
        modelRequest.onResumeGameTimerRequested()
    }

    fun onClearInstance() {
        gameBackend.removeListener(this)
    }

    override fun onSymbolUpdated(newSymbol: HiraganaSymbol) {
        _symbol = newSymbol
        listeners.forEach { it.onCurrentHiraganaSymbolUpdated(newSymbol) }
    }

    override fun onGameScoreUpdated(newScore: Int) {
        _score = newScore
        listeners.forEach { it.onGameScoreUpdated(newScore) }
    }

    override fun onGameProgressUpdated(updatedProgress: Int) {
        listeners.forEach { it.onGameProgressUpdated(updatedProgress) }
    }

    override fun onGameCountdownTimeUpdated(updatedCountdownTime: Int) {
        listeners.forEach { it.onGameCountDownTimeUpdated(updatedCountdownTime) }
    }

    override fun onRomanizationGroupUpdated(updatedRomanizationGroupList: List<String>) {
        listeners.forEach { it.onRomanizationGroupUpdated(updatedRomanizationGroupList) }
    }

    override fun onCorrectAnswer() {
        listeners.forEach { it.onCorrectAnswer() }
    }

    override fun onWrongAnswer() {
        listeners.forEach { it.onWrongAnswer() }
    }

    override fun onGameTimeOver() {
        listeners.forEach { it.onGameTimeOver() }
    }

    override fun onGameFinished() {
        _gameFinished = true
    }
}