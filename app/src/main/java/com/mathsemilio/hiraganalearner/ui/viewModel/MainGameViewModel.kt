package com.mathsemilio.hiraganalearner.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mathsemilio.hiraganalearner.data.hiraganaLetters
import com.mathsemilio.hiraganalearner.data.model.Hiragana

private const val TAG_MAIN_GAME_SCREEN_VM = "MainGameViewModel"

/**
 * ViewModel class that implements most of the game's logic.
 */
class MainGameViewModel : ViewModel() {

    //==========================================================================================
    // MutableLiveData variables for the UI elements
    //==========================================================================================
    private val _currentHiraganaLetterDrawableId = MutableLiveData<Int>()
    val currentHiraganaLetterDrawableId: LiveData<Int>
        get() = _currentHiraganaLetterDrawableId

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

    private var lastHiraganaLetterDrawableId: Int = 0
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
        Log.i(TAG_MAIN_GAME_SCREEN_VM, "startGame: Game Started")

        // Shuffling the hiraganaLettersList list
        hiraganaLettersList.shuffle()

        // Getting the first drawableSymbolId and romanization from the list
        _currentHiraganaLetterDrawableId.value = hiraganaLettersList.first().drawableSymbolId
        _currentHiraganaLetterRomanization.value = hiraganaLettersList.first().romanization.also {
            Log.d(TAG_MAIN_GAME_SCREEN_VM, "startGame: First letter: $it")
        }

        // Getting the last drawableSymbolId and romanization from the list
        lastHiraganaLetterDrawableId = hiraganaLettersList.last().drawableSymbolId
        lastHiraganaLetterRomanization = hiraganaLettersList.last().romanization.also {
            Log.d(TAG_MAIN_GAME_SCREEN_VM, "startGame: Last letter: $it")
        }

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
            Log.d(TAG_MAIN_GAME_SCREEN_VM, "checkUserInput: Answer correct")
            _eventCorrectAnswer.value = true

            updateGameScore()
        } else {
            Log.d(TAG_MAIN_GAME_SCREEN_VM, "checkUserInput: Incorrect answer")
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
        _currentHiraganaLetterDrawableId.value = hiraganaLettersList.first().drawableSymbolId
        _currentHiraganaLetterRomanization.value = hiraganaLettersList.first().romanization.also {
            Log.d(TAG_MAIN_GAME_SCREEN_VM, "getNextLetter: Next letter: $it")
        }

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
        Log.d(TAG_MAIN_GAME_SCREEN_VM, "getLastLetter: Setting last letter")
        /*
         Setting the value of the current hiragana letter as the value of the last letter
         from the list
        */
        _currentHiraganaLetterDrawableId.value = lastHiraganaLetterDrawableId
        _currentHiraganaLetterRomanization.value = lastHiraganaLetterRomanization

        if (_currentHiraganaLetterRomanization.value == selectedRomanization) {
            Log.d(TAG_MAIN_GAME_SCREEN_VM, "getLastLetter: Correct answer, game finished")
            _eventCorrectAnswer.value = true

            updateGameScore()

            // Setting the value of _eventGameFinished as TRUE to finish the game
            _eventGameFinished.value = true
        } else {
            Log.d(TAG_MAIN_GAME_SCREEN_VM, "getLastLetter: Incorrect answer, game finished")
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
        Log.d(TAG_MAIN_GAME_SCREEN_VM, "updateGameScore: Incrementing game score")
        _gameScore.value = (_gameScore.value)?.inc().also {
            Log.d(TAG_MAIN_GAME_SCREEN_VM, "updateGameScore: New value: $it")
        }
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
        val hiraganaRomanizationList: List<String> = listOf(
            "A", "I", "U", "E", "O", "KA", "KI", "KU", "KE", "KO", "SA", "SHI", "SU", "SE", "SO",
            "TA", "CHI", "TSU", "TE", "TO", "NA", "NI", "NU", "NE", "NO", "HA", "HI", "FU", "HE",
            "HO", "MA", "MI", "MU", "ME", "MO", "YA", "YU", "YO", "RA", "RI", "RU", "RE", "RO",
            "WA", "WI", "WE", "WO", "N"
        )

        val filteredRomanizationList =
            hiraganaRomanizationList.filterNot { it == _currentHiraganaLetterRomanization.value }

        /*
        Variables for storing the random number generated from the generateRandomNumber function.
        For the last three variables, the parameter passed to function is the value from the
        variable above.
        */
        val radioButton1RandomIndexValue = generateRandomNumber(null).also {
            Log.d(
                TAG_MAIN_GAME_SCREEN_VM,
                "generateRadioButtonRomanization: radioButton1RomanizationIndex value: $it"
            )
        }
        val radioButton2RandomIndexValue = generateRandomNumber(radioButton1RandomIndexValue).also {
            Log.d(
                TAG_MAIN_GAME_SCREEN_VM,
                "generateRadioButtonRomanization: radioButton1RomanizationIndex value: $it"
            )
        }
        val radioButton3RandomIndexValue = generateRandomNumber(radioButton2RandomIndexValue).also {
            Log.d(
                TAG_MAIN_GAME_SCREEN_VM,
                "generateRadioButtonRomanization: radioButton1RomanizationIndex value: $it"
            )
        }
        val radioButton4RandomIndexValue = generateRandomNumber(radioButton3RandomIndexValue).also {
            Log.d(
                TAG_MAIN_GAME_SCREEN_VM,
                "generateRadioButtonRomanization: radioButton1RomanizationIndex value: $it"
            )
        }

        /*
        Getting a romanization from the list for each of the radio buttons by utilizing the
        index values generated from the variables above.
        */
        _radioButton1Romanization.value = filteredRomanizationList[radioButton1RandomIndexValue]
        _radioButton2Romanization.value = filteredRomanizationList[radioButton2RandomIndexValue]
        _radioButton3Romanization.value = filteredRomanizationList[radioButton3RandomIndexValue]
        _radioButton4Romanization.value = filteredRomanizationList[radioButton4RandomIndexValue]

        /*
        Generating a random number between 0 and 4, and based on that number, a radio button
        will be selected to contain the current romanization for the letter on the screen.
        */
        when ((0 until 4).random()) {
            0 -> _radioButton1Romanization.value = _currentHiraganaLetterRomanization.value.also {
                Log.d(
                    TAG_MAIN_GAME_SCREEN_VM,
                    "generateRadioButtonRomanization: Radio Button 1 selected to contain the " +
                            "correct answer"
                )
            }
            1 -> _radioButton2Romanization.value = _currentHiraganaLetterRomanization.value.also {
                Log.d(
                    TAG_MAIN_GAME_SCREEN_VM,
                    "generateRadioButtonRomanization: Radio Button 2 selected to contain the " +
                            "correct answer"
                )
            }
            2 -> _radioButton3Romanization.value = _currentHiraganaLetterRomanization.value.also {
                Log.d(
                    TAG_MAIN_GAME_SCREEN_VM,
                    "generateRadioButtonRomanization: Radio Button 3 selected to contain the " +
                            "correct answer"
                )
            }
            3 -> _radioButton4Romanization.value = _currentHiraganaLetterRomanization.value.also {
                Log.d(
                    TAG_MAIN_GAME_SCREEN_VM,
                    "generateRadioButtonRomanization: Radio Button 4 selected to contain the " +
                            "correct answer"
                )
            }
        }
    }

    //==========================================================================================
    // generateRandomNumber function
    //==========================================================================================
    /**
     * Function that returns random numbers between 0 and 47 and excludes a number that
     * matches the function parameter.
     */
    private fun generateRandomNumber(previousNumber: Int?): Int {
        return (0 until 47).filterNot {
            it == previousNumber
        }.random()
    }
}