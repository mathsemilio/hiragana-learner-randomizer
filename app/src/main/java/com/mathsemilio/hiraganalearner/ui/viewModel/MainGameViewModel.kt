package com.mathsemilio.hiraganalearner.ui.viewModel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mathsemilio.hiraganalearner.data.hiraganaSymbolsList
import com.mathsemilio.hiraganalearner.data.model.HiraganaSymbol
import com.mathsemilio.hiraganalearner.others.GAME_DIFFICULTY_VALUE_BEGINNER
import com.mathsemilio.hiraganalearner.others.GAME_DIFFICULTY_VALUE_MEDIUM
import kotlin.random.Random

/**
 * ViewModel class that implements most of the games and UI logic.
 */
class MainGameViewModel(gameDifficultyValue: Int) : ViewModel() {

    companion object {
        const val ONE_SECOND = 1000L
        const val COUNTDOWN_TIME_BEGINNER = 15000L
        const val COUNTDOWN_TIME_MEDIUM = 10000L
        const val COUNTDOWN_TIME_HARD = 5000L

        const val PROGRESS_BAR_MAX_BEGINNER = 14
        const val PROGRESS_BAR_MAX_MEDIUM = 9
        const val PROGRESS_BAR_MAX_HARD = 4
    }

    //==========================================================================================
    // MutableLiveData variables for the UI elements
    //==========================================================================================
    private val _currentHiraganaLetterString = MutableLiveData<String>()
    val currentHiraganaLetterString: LiveData<String>
        get() = _currentHiraganaLetterString

    private val _currentHiraganaLetterRomanization = MutableLiveData<String>()
    val currentHiraganaLetterRomanization: LiveData<String>
        get() = _currentHiraganaLetterRomanization

    private val _chip1StringRomanization = MutableLiveData<String>()
    val chip1StringRomanization: LiveData<String>
        get() = _chip1StringRomanization

    private val _chip2StringRomanization = MutableLiveData<String>()
    val chip2StringRomanization: LiveData<String>
        get() = _chip2StringRomanization

    private val _chip3StringRomanization = MutableLiveData<String>()
    val chip3StringRomanization: LiveData<String>
        get() = _chip3StringRomanization

    private val _chip4StringRomanization = MutableLiveData<String>()
    val chip4StringRomanization: LiveData<String>
        get() = _chip4StringRomanization

    private val _gameProgress = MutableLiveData<Int>()
    val gameProgress: LiveData<Int>
        get() = _gameProgress

    private val _gameScore = MutableLiveData<Int>()
    val gameScore: LiveData<Int>
        get() = _gameScore

    private val _currentGameTime = MutableLiveData<Long>()
    val currentGameTime: LiveData<Long>
        get() = _currentGameTime

    val currentGameTimeInt = Transformations.map(currentGameTime) { currentGameTime ->
        currentGameTime.toInt()
    }

    //==========================================================================================
    // MutableLiveData variables for game events
    //==========================================================================================
    private val _eventCorrectAnswer = MutableLiveData<Boolean>()
    val eventCorrectAnswer: LiveData<Boolean>
        get() = _eventCorrectAnswer

    private val _eventTimeOver = MutableLiveData<Boolean>()
    val eventTimeOver: LiveData<Boolean>
        get() = _eventTimeOver

    private val _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished: LiveData<Boolean>
        get() = _eventGameFinished

    //==========================================================================================
    // Other variables
    //==========================================================================================
    val hiraganaSymbolsMutableList: MutableList<HiraganaSymbol> =
        hiraganaSymbolsList.toMutableList()

    private var lastHiraganaLetterDrawableId = ""
    private var lastHiraganaLetterRomanization = ""

    lateinit var countDownTimer: CountDownTimer
    var gameTimerProgressBarValue: Int
    private var difficultyCountDownTime: Long

    init {
        _gameProgress.value = 0

        _gameScore.value = 0

        _eventGameFinished.value = false

        difficultyCountDownTime = when (gameDifficultyValue) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> COUNTDOWN_TIME_BEGINNER
            GAME_DIFFICULTY_VALUE_MEDIUM -> COUNTDOWN_TIME_MEDIUM
            else -> COUNTDOWN_TIME_HARD
        }

        gameTimerProgressBarValue = when (gameDifficultyValue) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> PROGRESS_BAR_MAX_BEGINNER
            GAME_DIFFICULTY_VALUE_MEDIUM -> PROGRESS_BAR_MAX_MEDIUM
            else -> PROGRESS_BAR_MAX_HARD
        }

        startGame()
    }

    /**
     * Performs essential tasks necessary for starting the game.
     */
    private fun startGame() {
        // Shuffling the hiraganaLettersList list
        hiraganaSymbolsMutableList.shuffle()

        // Getting the first drawableSymbolId and romanization from the list
        _currentHiraganaLetterString.value = hiraganaSymbolsMutableList.first().symbol
        _currentHiraganaLetterRomanization.value = hiraganaSymbolsMutableList.first().romanization

        // Getting the last drawableSymbolId and romanization from the list
        lastHiraganaLetterDrawableId = hiraganaSymbolsMutableList.last().symbol
        lastHiraganaLetterRomanization = hiraganaSymbolsMutableList.last().romanization

        generateChipGroupRomanization()

        startGameTimer(difficultyCountDownTime)
    }

    /**
     * Sets up the game timer.
     *
     * @param countDownTime Long to be used as the countdown time for the timer.
     */
    fun startGameTimer(countDownTime: Long) {
        countDownTimer = object : CountDownTimer(countDownTime, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _currentGameTime.value = (millisUntilFinished / ONE_SECOND)
            }

            override fun onFinish() {
                cancel()
                _eventTimeOver.value = true
            }
        }
        countDownTimer.start()
    }

    /**
     * Checks the user's input (answer).
     *
     * @param selectedRomanization String of the romanization to be evaluated.
     */
    fun checkUserInput(selectedRomanization: String) {
        /*
        Checking if the current romanization equals the selected romanization, if it is, the
        answer is correct and the game score is updated, else it's incorrect
        */
        if (_currentHiraganaLetterRomanization.value == selectedRomanization) {
            _eventCorrectAnswer.value = true

            updateGameScore()
        } else {
            _eventCorrectAnswer.value = false
        }
    }

    /**
     * Removes the current letter, and gets the next one from the list. It also calls other
     * functions to set up the UI for the game.
     */
    fun getNextLetter() {
        // Removing the first element (Hiragana letter) from the list
        hiraganaSymbolsMutableList.removeAt(0)

        // Getting the first letter from the hiraganaLettersList list
        _currentHiraganaLetterString.value = hiraganaSymbolsMutableList.first().symbol
        _currentHiraganaLetterRomanization.value = hiraganaSymbolsMutableList.first().romanization

        generateChipGroupRomanization()

        startGameTimer(difficultyCountDownTime)

        updateGameProgress()
    }

    /**
     * Gets the last letter from the list and sets its contents to the UI. It also checks the
     * user input and finishes the game.
     *
     * @param selectedRomanization String of the current checked chip button.
     */
    fun getLastLetter(selectedRomanization: String) {
        /*
         Setting the value of the current hiragana letter as the value of the last letter
         from the list
        */
        _currentHiraganaLetterString.value = lastHiraganaLetterDrawableId
        _currentHiraganaLetterRomanization.value = lastHiraganaLetterRomanization

        if (_currentHiraganaLetterRomanization.value == selectedRomanization) {
            _eventCorrectAnswer.value = true

            updateGameScore()

            updateGameProgress()

            // Setting the value of _eventGameFinished as TRUE to finish the game
            _eventGameFinished.value = true
        } else {
            updateGameProgress()

            _eventCorrectAnswer.value = false

            // Setting the value of _eventGameFinished as TRUE to finish the game
            _eventGameFinished.value = true
        }
    }

    /**
     * Increments the game score by 1 point.
     */
    private fun updateGameScore() {
        _gameScore.value = (_gameScore.value)?.inc()
    }

    /**
     * Increments the game progress by 1.
     */
    private fun updateGameProgress() {
        _gameProgress.value = (_gameProgress.value)?.inc()
    }

    /**
     * Generates random romanizations for the chip buttons. It also selects which of them will
     * receive the current letter romanization (the correct answer).
     */
    private fun generateChipGroupRomanization() {
        // List containing romanizations to be used as distractions
        val hiraganaRomanizationList = listOf(
            "A", "I", "U", "E", "O", "KA", "KI", "KU", "KE", "KO", "SA", "SHI", "SU", "SE", "SO",
            "TA", "CHI", "TSU", "TE", "TO", "NA", "NI", "NU", "NE", "NO", "HA", "HI", "FU", "HE",
            "HO", "MA", "MI", "MU", "ME", "MO", "YA", "YU", "YO", "RA", "RI", "RU", "RE", "RO",
            "WA", "WI", "WE", "WO", "N"
        )

        /*
         List that takes the hiraganaRomanizationList, applies a filter (to remove the
         romanization that matches the current one), and shuffles it.
        */
        val filteredList =
            hiraganaRomanizationList.filterNot { it == _currentHiraganaLetterRomanization.value }
                .shuffled()

        // Getting a random romanization for each chip from the filteredList
        _chip1StringRomanization.value = filteredList.slice(0..13).random()

        _chip2StringRomanization.value = filteredList.slice(14..27).random()

        _chip3StringRomanization.value = filteredList.slice(28..42).random()

        _chip4StringRomanization.value = filteredList.slice(43..46).random()

        /*
        Generating a random number between 0 and 4, and based on that number, a chip will be
        selected to contain the current romanization for the letter on the screen.
        */
        when (Random.nextInt(4)) {
            0 -> _chip1StringRomanization.value = _currentHiraganaLetterRomanization.value
            1 -> _chip2StringRomanization.value = _currentHiraganaLetterRomanization.value
            2 -> _chip3StringRomanization.value = _currentHiraganaLetterRomanization.value
            3 -> _chip4StringRomanization.value = _currentHiraganaLetterRomanization.value
        }
    }

    override fun onCleared() {
        super.onCleared()
        countDownTimer.cancel()
    }
}