package com.mathsemilio.hiraganalearner.ui.viewModel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mathsemilio.hiraganalearner.others.*
import kotlin.random.Random

/**
 * ViewModel class that handles the Game and UI logic for the GameMainScreen fragment
 */
class MainGameViewModel : ViewModel() {

    //==========================================================================================
    // UI elements
    //==========================================================================================
    private val _currentHiraganaSymbol = MutableLiveData<String>()
    val currentHiraganaSymbol: LiveData<String>
        get() = _currentHiraganaSymbol

    private val _currentHiraganaRomanization = MutableLiveData<String>()
    val currentHiraganaRomanization: LiveData<String>
        get() = _currentHiraganaRomanization

    private val _chipButton1Romanization = MutableLiveData<String>()
    val chipButton1Romanization: LiveData<String>
        get() = _chipButton1Romanization

    private val _chipButton2Romanization = MutableLiveData<String>()
    val chipButton2Romanization: LiveData<String>
        get() = _chipButton2Romanization

    private val _chipButton3Romanization = MutableLiveData<String>()
    val chipButton3Romanization: LiveData<String>
        get() = _chipButton3Romanization

    private val _chipButton4Romanization = MutableLiveData<String>()
    val chipButton4Romanization: LiveData<String>
        get() = _chipButton4Romanization

    private val _gameScore = MutableLiveData(0)
    val gameScore: LiveData<Int>
        get() = _gameScore

    private val _gameProgress = MutableLiveData(0)
    val gameProgress: LiveData<Int>
        get() = _gameProgress

    private val _currentGameTime = MutableLiveData<Long>()
    val currentGameTimeInt = Transformations.map(_currentGameTime) { it.toInt() }

    //==========================================================================================
    // Game events
    //==========================================================================================
    private val _eventButtonPauseClicked = MutableLiveData<Boolean?>()
    val eventButtonPauseClicked: LiveData<Boolean?>
        get() = _eventButtonPauseClicked

    private val _eventButtonExitGameClicked = MutableLiveData<Boolean?>()
    val eventButtonExitGameClicked: LiveData<Boolean?>
        get() = _eventButtonExitGameClicked

    private val _eventIsAnswerCorrect = MutableLiveData<Boolean?>()
    val eventIsAnswerCorrect: LiveData<Boolean?>
        get() = _eventIsAnswerCorrect

    private val _eventTimeOver = MutableLiveData<Boolean?>()
    val eventTimeOver: LiveData<Boolean?>
        get() = _eventTimeOver

    var gameFinished = false

    //==========================================================================================
    // Other variables
    //==========================================================================================
    private lateinit var countDownTimer: CountDownTimer
    private val hiraganaSymbolsMutableList = hiraganaSymbolsList.toMutableList()

    private var countDownTimeBasedOnDifficulty = 0L

    //==========================================================================================
    // Functions to handle the Game logic
    //==========================================================================================
    private fun startGame(difficultyValue: Int) {
        countDownTimeBasedOnDifficulty = when (difficultyValue) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> COUNTDOWN_TIME_BEGINNER
            GAME_DIFFICULTY_VALUE_MEDIUM -> COUNTDOWN_TIME_MEDIUM
            else -> COUNTDOWN_TIME_HARD
        }

        hiraganaSymbolsMutableList.shuffle()

        _currentHiraganaSymbol.value = hiraganaSymbolsMutableList.first().symbol
        _currentHiraganaRomanization.value = hiraganaSymbolsMutableList.first().romanization

        setChipGroupRomanizations()
        startTimer(countDownTimeBasedOnDifficulty)
    }

    private fun startTimer(countDownTime: Long) {
        countDownTimer = object : CountDownTimer(countDownTime, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _currentGameTime.value = (millisUntilFinished / ONE_SECOND)
            }

            override fun onFinish() {
                _currentGameTime.value = 0
                _eventTimeOver.value = true
            }
        }
        countDownTimer.start()
    }

    private fun cancelTimer() {
        countDownTimer.cancel()
    }

    private fun restartTimer() {
        _currentGameTime.value?.let { startTimer(it.times(1000L)) }
            ?: startTimer(countDownTimeBasedOnDifficulty)
    }

    private fun checkAnswer(selectedRomanization: String) {
        if (selectedRomanization == _currentHiraganaRomanization.value) {
            incrementScore()
            _eventIsAnswerCorrect.value = true
        } else {
            _eventIsAnswerCorrect.value = false
        }
    }

    private fun getNextSymbol() {
        incrementProgress()

        hiraganaSymbolsMutableList.removeAt(0)

        _currentHiraganaSymbol.value = hiraganaSymbolsMutableList.first().symbol
        _currentHiraganaRomanization.value = hiraganaSymbolsMutableList.first().romanization

        if (hiraganaSymbolsMutableList.size == 1) {
            setChipGroupRomanizations()
            startTimer(countDownTimeBasedOnDifficulty)
            gameFinished = true
        } else {
            setChipGroupRomanizations()
            startTimer(countDownTimeBasedOnDifficulty)
        }
    }

    private fun incrementScore() {
        _gameScore.value = (_gameScore.value)?.inc()
    }

    private fun incrementProgress() {
        _gameProgress.value = (_gameProgress.value)?.inc()
    }

    private fun setChipGroupRomanizations() {
        val romanizationList = arrayOf(
            "A", "I", "U", "E", "O", "KA", "KI", "KU", "KE", "KO", "SA", "SHI", "SU", "SE", "SO",
            "TA", "CHI", "TSU", "TE", "TO", "NA", "NI", "NU", "NE", "NO", "HA", "HI", "FU", "HE",
            "HO", "MA", "MI", "MU", "ME", "MO", "YA", "YU", "YO", "RA", "RI", "RU", "RE", "RO",
            "WA", "WI", "WE", "WO", "N"
        ).let { romanizationList ->
            romanizationList.shuffle()
            romanizationList.filterNot { it == _currentHiraganaRomanization.value }
        }

        _chipButton1Romanization.value = romanizationList.slice(0..11).random()
        _chipButton2Romanization.value = romanizationList.slice(12..23).random()
        _chipButton3Romanization.value = romanizationList.slice(24..35).random()
        _chipButton4Romanization.value = romanizationList.slice(36..46).random()

        setChipButtonWithCorrectAnswer()
    }

    private fun setChipButtonWithCorrectAnswer() {
        when (Random.nextInt(4)) {
            0 -> _chipButton1Romanization.value = _currentHiraganaRomanization.value
            1 -> _chipButton2Romanization.value = _currentHiraganaRomanization.value
            2 -> _chipButton3Romanization.value = _currentHiraganaRomanization.value
            3 -> _chipButton4Romanization.value = _currentHiraganaRomanization.value
        }
    }

    //==========================================================================================
    // UI callbacks
    //==========================================================================================
    fun onStartGame(difficultyValue: Int) =
        startGame(difficultyValue)

    fun onButtonCheckAnswerClicked(selectedRomanization: String) {
        cancelTimer()
        checkAnswer(selectedRomanization)
    }

    fun onButtonPauseGameClicked() {
        cancelTimer()
        _eventButtonPauseClicked.value = true
    }

    fun onButtonExitGameClicked() {
        cancelTimer()
        _eventButtonExitGameClicked.value = true
    }

    fun onPauseGameCompleted() {
        _eventButtonPauseClicked.value = null
        restartTimer()
    }

    fun onExitGameCancelled() {
        _eventButtonExitGameClicked.value = null
        restartTimer()
    }

    fun onCheckAnswerCompleted() {
        _eventIsAnswerCorrect.value = null
        getNextSymbol()
    }

    fun onTimeOverCompleted() {
        _eventTimeOver.value = null
        getNextSymbol()
    }

    fun onFragmentPaused() = cancelTimer()

    fun onFragmentRestarted() = restartTimer()

    //==========================================================================================
    // OnCleared
    //==========================================================================================
    override fun onCleared() {
        cancelTimer()
        super.onCleared()
    }
}