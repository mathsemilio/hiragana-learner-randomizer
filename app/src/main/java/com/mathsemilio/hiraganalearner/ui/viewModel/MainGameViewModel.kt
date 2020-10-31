package com.mathsemilio.hiraganalearner.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mathsemilio.hiraganalearner.backend.BackendListener
import com.mathsemilio.hiraganalearner.backend.GameBackend

/**
 * ViewModel class that handles the UI logic for the MainGameScreen Fragment.
 */
class MainGameViewModel(difficultyValue: Int) : ViewModel(), BackendListener {

    var gameBackend: GameBackend? = null

    private val _eventButtonPauseClicked = MutableLiveData<Boolean?>()
    val eventButtonPauseClicked: LiveData<Boolean?>
        get() = _eventButtonPauseClicked

    private val _eventButtonExitGameClicked = MutableLiveData<Boolean?>()
    val eventButtonExitGameClicked: LiveData<Boolean?>
        get() = _eventButtonExitGameClicked

    private val _eventCorrectAnswer = MutableLiveData<Boolean?>()
    val eventCorrectAnswer: LiveData<Boolean?>
        get() = _eventCorrectAnswer

    private val _eventWrongAnswer = MutableLiveData<String?>()
    val eventWrongAnswer: LiveData<String?>
        get() = _eventWrongAnswer

    private val _eventTimeOver = MutableLiveData<String?>()
    val eventTimeOver: LiveData<String?>
        get() = _eventTimeOver

    var flagGameFinished = false

    init {
        gameBackend = GameBackend(difficultyValue)
        gameBackend?.backendEvents = this
    }

    fun getUserInput(selectedRomanization: String) {
        pauseGameTimer()
        gameBackend?.checkAnswer(selectedRomanization)
    }

    fun pauseGame() {
        pauseGameTimer()
        _eventButtonPauseClicked.value = true
    }

    fun exitGame() {
        pauseGameTimer()
        _eventButtonExitGameClicked.value = true
    }

    fun checkUserInputCompleted() {
        gameBackend?.getNextSymbol()
    }

    fun handleWrongAnswerEvent() {
        gameBackend?.getNextSymbol()
        _eventWrongAnswer.value = null
    }

    fun handleTimeOverEvent() {
        gameBackend?.getNextSymbol()
        _eventTimeOver.value = null
    }

    fun pauseGameCompleted() {
        restartGameTimer()
        _eventButtonPauseClicked.value = null
    }

    fun exitGameCancelled() {
        restartGameTimer()
        _eventButtonExitGameClicked.value = null
    }

    fun pauseGameTimer() = gameBackend?.cancelTimer()

    fun restartGameTimer() = gameBackend?.restartTimer()

    //==========================================================================================
    // BackendListener implementations
    //==========================================================================================
    override fun onCorrectAnswer() {
        _eventCorrectAnswer.value = true
    }

    override fun onWrongAnswer(correctRomanization: String) {
        _eventWrongAnswer.value = correctRomanization
    }

    override fun onCountDownTimeFinished(romanizationAnswer: String) {
        _eventTimeOver.value = romanizationAnswer
    }

    override fun onGameFinished() {
        flagGameFinished = true
    }

    //==========================================================================================
    // onCleared lifecycle callback
    //==========================================================================================
    override fun onCleared() {
        gameBackend?.cancelTimer()
        gameBackend = null
        super.onCleared()
    }
}