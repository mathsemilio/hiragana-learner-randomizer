package com.mathsemilio.hiraganalearner.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mathsemilio.hiraganalearner.data.hiraganaLetters
import com.mathsemilio.hiraganalearner.data.model.Hiragana

/**
 * ViewModel class that implements most of the game's logic.
 */
class MainGameViewModel : ViewModel() {

    //==========================================================================================
    // MutableLiveData variables for the UI elements
    //==========================================================================================
    private val _currentHiraganaLetterString = MutableLiveData<String>()
    val currentHiraganaLetterString: LiveData<String>
        get() = _currentHiraganaLetterString

    private val _currentHiraganaLetterRomanization = MutableLiveData<String>()
    val currentHiraganaLetterRomanization: LiveData<String>
        get() = _currentHiraganaLetterRomanization

    private val _radioButton1Romanization = MutableLiveData<String>()
    val radioButton1Romanization: LiveData<String>
        get() = _radioButton1Romanization

    private val _radioButton2Romanization = MutableLiveData<String>()
    val radioButton2Romanization: LiveData<String>
        get() = _radioButton2Romanization

    private val _radioButton3Romanization = MutableLiveData<String>()
    val radioButton3Romanization: LiveData<String>
        get() = _radioButton3Romanization

    private val _radioButton4Romanization = MutableLiveData<String>()
    val radioButton4Romanization: LiveData<String>
        get() = _radioButton4Romanization

    private val _gameScore = MutableLiveData<Short>()
    val gameScore: LiveData<Short>
        get() = _gameScore

    //==========================================================================================
    // MutableLiveData variables for game events
    //==========================================================================================
    private val _eventCorrectAnswer = MutableLiveData<Boolean>()
    val eventCorrectAnswer: LiveData<Boolean>
        get() = _eventCorrectAnswer

    private val _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished: LiveData<Boolean>
        get() = _eventGameFinished

    //==========================================================================================
    // Other variables
    //==========================================================================================
    val hiraganaLettersList: MutableList<Hiragana> = hiraganaLetters.toMutableList()

    private var lastHiraganaLetterDrawableId: String? = null
    private var lastHiraganaLetterRomanization: String? = null

    //==========================================================================================
    // init block
    //==========================================================================================
    init {
        _gameScore.value = 0

        _eventGameFinished.value = false

        startGame()
    }

    //==========================================================================================
    // startGame function
    //==========================================================================================
    /**
     * Private function that is responsible for key tasks necessary for starting the game
     */
    private fun startGame() {
        // Shuffling the hiraganaLettersList list
        hiraganaLettersList.shuffle()

        // Getting the first drawableSymbolId and romanization from the list
        _currentHiraganaLetterString.value = hiraganaLettersList.first().letter
        _currentHiraganaLetterRomanization.value = hiraganaLettersList.first().romanization

        // Getting the last drawableSymbolId and romanization from the list
        lastHiraganaLetterDrawableId = hiraganaLettersList.last().letter
        lastHiraganaLetterRomanization = hiraganaLettersList.last().romanization

        generateRadioButtonRomanization()
    }

    //==========================================================================================
    // checkUserInput function
    //==========================================================================================
    /**
     * Function responsible for checking the user's input (answer).
     *
     * @param selectedRomanization - String of the current checked radio button
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

    //==========================================================================================
    // getNextLetter function
    //==========================================================================================
    /**
     * Function responsible for removing the current letter, and getting the next one from the
     * list.
     */
    fun getNextLetter() {
        // Removing the first element (Hiragana letter) from the list
        hiraganaLettersList.removeAt(0)

        // Getting the first letter from the hiraganaLettersList list
        _currentHiraganaLetterString.value = hiraganaLettersList.first().letter
        _currentHiraganaLetterRomanization.value = hiraganaLettersList.first().romanization

        generateRadioButtonRomanization()
    }

    //==========================================================================================
    // getLastLetter function
    //==========================================================================================
    /**
     * Function responsible for getting the last letter from the list and setting its contents
     * to the UI. It also checks the user input and finishes the game.
     *
     * @param selectedRomanization - String of the current checked radio button
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

            // Setting the value of _eventGameFinished as TRUE to finish the game
            _eventGameFinished.value = true
        } else {
            _eventCorrectAnswer.value = false

            // Setting the value of _eventGameFinished as TRUE to finish the game
            _eventGameFinished.value = true
        }
    }

    //==========================================================================================
    // updateGameScore function
    //==========================================================================================
    /**
     * Function that increments the game score by 1.
     */
    private fun updateGameScore() {
        _gameScore.value = (_gameScore.value)?.inc()
    }

    //==========================================================================================
    // generateRadioButtonRomanization function
    //==========================================================================================
    /**
     * Function that generates random romanizations for the radio buttons. It also selects which
     * button will receive the current letter romanization (the correct answer).
     */
    private fun generateRadioButtonRomanization() {
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

        // Getting a random romanization for each radio button from the filteredList
        _radioButton1Romanization.value = filteredList.slice(0..13).random()

        _radioButton2Romanization.value = filteredList.slice(14..27).random()

        _radioButton3Romanization.value = filteredList.slice(28..42).random()

        _radioButton4Romanization.value = filteredList.slice(43..46).random()

        /*
        Generating a random number between 0 and 4, and based on that number, a radio button
        will be selected to contain the current romanization for the letter on the screen.
        */
        when ((0 until 4).random()) {
            0 -> _radioButton1Romanization.value = _currentHiraganaLetterRomanization.value
            1 -> _radioButton2Romanization.value = _currentHiraganaLetterRomanization.value
            2 -> _radioButton3Romanization.value = _currentHiraganaLetterRomanization.value
            3 -> _radioButton4Romanization.value = _currentHiraganaLetterRomanization.value
        }
    }
}