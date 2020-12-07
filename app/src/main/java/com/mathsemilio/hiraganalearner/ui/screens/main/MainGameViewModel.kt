package com.mathsemilio.hiraganalearner.ui.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mathsemilio.hiraganalearner.data.model.HiraganaSymbol
import com.mathsemilio.hiraganalearner.logic.backend.GameBackend
import com.mathsemilio.hiraganalearner.logic.backend.IBackendObserver
import com.mathsemilio.hiraganalearner.logic.event.BackendEvent
import com.mathsemilio.hiraganalearner.logic.event.GameEvent
import com.mathsemilio.hiraganalearner.logic.event.UIRequest

class MainGameViewModel : ViewModel(), IBackendObserver {

    private val gameBackend = GameBackend()

    private val _currentHiraganaSymbol = MutableLiveData<String>()
    private val _currentHiraganaRomanization = MutableLiveData<String>()
    private val _chipButton1Romanization = MutableLiveData<String>()
    private val _chipButton2Romanization = MutableLiveData<String>()
    private val _chipButton3Romanization = MutableLiveData<String>()
    private val _chipButton4Romanization = MutableLiveData<String>()
    private val _score = MutableLiveData(0)
    private val _progress = MutableLiveData(0)
    private val _currentTime = MutableLiveData<Int>()
    private val _gameEvent = MutableLiveData<GameEvent?>()

    val currentHiraganaSymbol: LiveData<String> get() = _currentHiraganaSymbol
    val currentHiraganaRomanization: LiveData<String> get() = _currentHiraganaRomanization
    val chipButton1Romanization: LiveData<String> get() = _chipButton1Romanization
    val chipButton2Romanization: LiveData<String> get() = _chipButton2Romanization
    val chipButton3Romanization: LiveData<String> get() = _chipButton3Romanization
    val chipButton4Romanization: LiveData<String> get() = _chipButton4Romanization
    val score: LiveData<Int> get() = _score
    val progress: LiveData<Int> get() = _progress
    val currentTime: LiveData<Int> get() = _currentTime
    val gameEvent: LiveData<GameEvent?> get() = _gameEvent

    var gameIsFinished = false

    init {
        gameBackend.registerObserver(this)
    }

    fun startGame(difficultyValue: Int) {
        handleUIRequest(UIRequest.StartGame(difficultyValue))
    }

    fun checkAnswer(selectedRomanization: String) {
        handleUIRequest(UIRequest.CheckAnswer(selectedRomanization))
    }

    fun getNextSymbol() {
        handleUIRequest(UIRequest.GetNextSymbol)
    }

    fun pauseGame() {
        pauseTimer()
        _gameEvent.value = GameEvent.Paused
    }

    fun pauseTimer() {
        handleUIRequest(UIRequest.PauseTimer)
    }

    fun resumeTimer() {
        handleUIRequest(UIRequest.RestartTimer)
    }

    fun exitGame() {
        pauseTimer()
        _gameEvent.value = GameEvent.Exit
    }

    fun onGameEventHandled() {
        _gameEvent.value = null
    }

    private fun handleUIRequest(uiRequest: UIRequest) {
        gameBackend.getUIRequest(uiRequest)
    }

    private fun setNewSymbol(newSymbol: HiraganaSymbol) {
        _currentHiraganaSymbol.value = newSymbol.symbol
        _currentHiraganaRomanization.value = newSymbol.romanization
    }

    private fun updateScore(newScore: Int) {
        _score.value = newScore
    }

    private fun updateProgress(newProgressValue: Int) {
        _progress.value = newProgressValue
    }

    private fun updateCountDownTime(newGameTimeValue: Int) {
        _currentTime.value = newGameTimeValue
    }

    private fun updateRomanizationGroup(updatedRomanizationGroupList: List<String>) {
        _chipButton1Romanization.value = updatedRomanizationGroupList[0]
        _chipButton2Romanization.value = updatedRomanizationGroupList[1]
        _chipButton3Romanization.value = updatedRomanizationGroupList[2]
        _chipButton4Romanization.value = updatedRomanizationGroupList[3]
    }

    override fun onBackendEvent(event: BackendEvent) {
        when (event) {
            is BackendEvent.NewSymbol ->
                setNewSymbol(event.currentSymbol)
            is BackendEvent.GameScoreUpdated ->
                updateScore(event.score)
            is BackendEvent.GameProgressUpdated ->
                updateProgress(event.progressValue)
            is BackendEvent.GameCountDownTimeUpdated ->
                updateCountDownTime(event.countDownTimeValue)
            is BackendEvent.RomanizationGroupUpdated ->
                updateRomanizationGroup(event.romanizationGroupList)
            BackendEvent.CorrectAnswer ->
                _gameEvent.value = GameEvent.AnswerIsCorrect
            BackendEvent.WrongAnswer ->
                _gameEvent.value = GameEvent.AnswerIsWrong
            BackendEvent.GameTimeOver ->
                _gameEvent.value = GameEvent.TimeIsOver
            BackendEvent.GameFinished ->
                gameIsFinished = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        gameBackend.getUIRequest(UIRequest.PauseTimer)
        gameBackend.removeObserver(this)
    }
}