package com.mathsemilio.hiraganalearner.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mathsemilio.hiraganalearner.data.hiraganaSymbol
import com.mathsemilio.hiraganalearner.data.model.HiraganaSymbol
import com.mathsemilio.hiraganalearner.util.TAG_MAIN_GAME_VIEW_MODEL

/**
 * ViewModel class which contains most of the game's logic.
 * Many properties in this class are declared in form of a backing property. For more info on
 * Kotlin's properties and fields, please refer to this:
 * @link https://kotlinlang.org/docs/reference/properties.html
 */
class MainGameViewModel : ViewModel() {

    //==========================================================================================
    // MutableLiveData variables for the UI elements
    //==========================================================================================
    // Backing property to store the value of the current hiragana letter drawable ID
    private val _currentHiraganaLetterDrawableId = MutableLiveData<Int>()
    val currentHiraganaLetterDrawableId: LiveData<Int>
        get() = _currentHiraganaLetterDrawableId

    // Backing property to store the value of the current hiragana letter romanization
    private val _currentHiraganaLetterRomanization = MutableLiveData<String>()
    val currentHiraganaLetterRomanization: LiveData<String>
        get() = _currentHiraganaLetterRomanization

    // Backing property to store the value of a romanization for the first radio button in the
    // UI
    private val _radioButton1Romanization = MutableLiveData<String>()
    val radioButton1Romanization: LiveData<String>
        get() = _radioButton1Romanization

    // Backing property to store the value of a romanization for the second radio button in the
    // UI
    private val _radioButton2Romanization = MutableLiveData<String>()
    val radioButton2Romanization: LiveData<String>
        get() = _radioButton2Romanization

    // Backing property to store the value of a romanization for the third radio button in the
    // UI
    private val _radioButton3Romanization = MutableLiveData<String>()
    val radioButton3Romanization: LiveData<String>
        get() = _radioButton3Romanization

    // Backing property to store the value of a romanization for the fourth radio button in the
    // UI
    private val _radioButton4Romanization = MutableLiveData<String>()
    val radioButton4Romanization: LiveData<String>
        get() = _radioButton4Romanization

    // Backing property to store the value of the game score
    private val _gameScore = MutableLiveData<Short>()
    val gameScore: LiveData<Short>
        get() = _gameScore

    //==========================================================================================
    // MutableLiveData variables for game events
    //==========================================================================================
    // Backing property to store a Boolean value to determine if the answer is correct or not
    private val _eventCorrectAnswer = MutableLiveData<Boolean>()
    val eventCorrectAnswer: LiveData<Boolean>
        get() = _eventCorrectAnswer

    // Backing property to store a Boolean value to determine if the answer is correct or not
    private val _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished: LiveData<Boolean>
        get() = _eventGameFinished

    //==========================================================================================
    // Other variables
    //==========================================================================================
    // Variable that stores a copy of the hiraganaSymbol list from the data package
    val hiraganaLettersList: MutableList<HiraganaSymbol> = hiraganaSymbol.toMutableList()

    // Variables for storing the last hiragana letter information shuffled from the list
    private var lastHiraganaLetterDrawableId: Int = 0
    private var lastHiraganaLetterRomanization: String? = null

    //==========================================================================================
    // init block
    //==========================================================================================
    init {
        // Initializing the game score value as 0
        Log.i(TAG_MAIN_GAME_VIEW_MODEL, "Initializing the gameScore value as 0")
        _gameScore.value = 0

        // Initializing the eventGameFinished value as false
        Log.i(TAG_MAIN_GAME_VIEW_MODEL, "Initializing the eventGameFinished value as false")
        _eventGameFinished.value = false

        // Calling the startGame function to start the game
        Log.i(TAG_MAIN_GAME_VIEW_MODEL, "startGame() called")
        startGame()
    }

    //==========================================================================================
    // startGame function
    //==========================================================================================
    /**
     * Private function that is responsible for key tasks necessary for starting the game
     */
    private fun startGame() {
        // Shuffling the hiraganaLettersList to randomize the order of the letters
        hiraganaLettersList.shuffle()

        // Setting the value of the _currentHiraganaLetterDrawableId as the first drawable symbol
        // ID from the list
        _currentHiraganaLetterDrawableId.value = hiraganaLettersList.first().drawableSymbolId

        // Setting the value of the _currentHiraganaLetterRomanization as the first romanization
        // from the list
        _currentHiraganaLetterRomanization.value = hiraganaLettersList.first().romanization.also {
            Log.d(TAG_MAIN_GAME_VIEW_MODEL, "First letter romanization: $it")
        }

        // Setting the value of the lastHiraganaLetterDrawableId as the last drawable ID in the
        // list
        lastHiraganaLetterDrawableId = hiraganaLettersList.last().drawableSymbolId

        // Setting the value of the lastHiraganaLetterRomanization as the last romanization in
        // the list
        lastHiraganaLetterRomanization = hiraganaLettersList.last().romanization.also {
            Log.d(TAG_MAIN_GAME_VIEW_MODEL, "Last letter romanization: $it")
        }

        Log.i(TAG_MAIN_GAME_VIEW_MODEL, "generateRadioButtonRomanization() called")
        // Calling generateRadioButtonRomanization function and passing the
        // _currentHiraganaLetterRomanization value as it's parameter
        generateRadioButtonRomanization(_currentHiraganaLetterRomanization.value.toString())
    }

    //==========================================================================================
    // checkUserInput function
    //==========================================================================================
    /**
     * Function that is responsible for checking if the user input (answer) is correct or not.
     *
     * @param correctRomanization - The current romanization stored in the
     * _currentHiraganaLetterRomanization variable
     * @param selectedRomanization - The text of the radio button selected by the user
     */
    fun checkUserInput(correctRomanization: String, selectedRomanization: String) {
        // When statement to check if the selectedRomanization is equal to the
        // correctRomanization or not
        when {
            // Case selectedRomanization is equal to the correctRomanization
            selectedRomanization == correctRomanization -> {
                Log.d(
                    TAG_MAIN_GAME_VIEW_MODEL,
                    "Correct Answer! Setting the value of the _eventGameFinished as true"
                )
                // Setting the value of the _eventCorrectAnswer variable as true
                _eventCorrectAnswer.value = true

                Log.i(TAG_MAIN_GAME_VIEW_MODEL, "updateGameScore() called")
                // Calling updateGameScore to update the game score
                updateGameScore()
            }
            // Case selectedRomanization is NOT equal to the correctRomanization
            selectedRomanization != correctRomanization -> {
                Log.d(
                    TAG_MAIN_GAME_VIEW_MODEL,
                    "Wrong Answer! Setting the value of the _eventGameFinished as false"
                )
                // Setting the value of the _eventCorrectAnswer variable as false
                _eventCorrectAnswer.value = false
            }
        }
    }

    //==========================================================================================
    // updateGameScore function
    //==========================================================================================
    /**
     * Private function that is responsible for incrementing the game score by 1, once the
     * user has selected the correct romanization for the current letter.
     */
    private fun updateGameScore() {
        _gameScore.value = (_gameScore.value)?.inc().also {
            Log.d(TAG_MAIN_GAME_VIEW_MODEL, "Updated game score: $it")
        }
    }

    //==========================================================================================
    // getNextLetter function
    //==========================================================================================
    /**
     * Private function that is responsible for removing a letter from list and getting a new
     * one from the list.
     */
    fun getNextLetter() {
        Log.i(TAG_MAIN_GAME_VIEW_MODEL, "getNextLetter called")

        // Removing the first element (letter) from the list
        hiraganaLettersList.removeAt(0)

        // Setting the value of the _currentHiraganaLetterDrawableId as the first drawable
        // symbol ID from the list
        _currentHiraganaLetterDrawableId.value = hiraganaLettersList.first().drawableSymbolId

        // Setting the value of the _currentHiraganaLetterRomanization as the first romanization
        // from the list
        _currentHiraganaLetterRomanization.value = hiraganaLettersList.first().romanization.also {
            Log.d(TAG_MAIN_GAME_VIEW_MODEL, "Next symbol romanization: $it")
        }

        Log.i(TAG_MAIN_GAME_VIEW_MODEL, "generateRadioButtonRomanization() called")
        // Calling generateRadioButtonRomanization()
        generateRadioButtonRomanization(
            _currentHiraganaLetterRomanization.value.toString()
        )
    }

    //==========================================================================================
    // getLastLetter function
    //==========================================================================================
    /**
     * Function that is responsible for getting the last letter in the list.
     * This function is also responsible for finishing the game.
     *
     * @param selectedRomanization - The text of the radio button selected by the user
     */
    fun getLastLetter(selectedRomanization: String) {
        // Setting the value of the _currentHiraganaLetterDrawableId variable as the value of
        // the lastHiraganaLetterDrawableId
        _currentHiraganaLetterDrawableId.value = lastHiraganaLetterDrawableId

        // Setting the value of the _currentHiraganaLetterRomanization variable as the value of
        // the lastHiraganaLetterRomanization
        _currentHiraganaLetterRomanization.value = lastHiraganaLetterRomanization

        // When statement to check if the _currentHiraganaLetterRomanization is equal to the
        // selectedRomanization in the UI
        when {
            // Case _currentHiraganaLetterRomanization.value equals selectedRomanization
            _currentHiraganaLetterRomanization.value == selectedRomanization -> {
                Log.i(
                    TAG_MAIN_GAME_VIEW_MODEL,
                    "Correct Answer! Setting the value of the _eventCorrectAnswer as true"
                )
                // Setting the value of the _eventCorrectAnswer variable as true
                _eventCorrectAnswer.value = true

                Log.i(TAG_MAIN_GAME_VIEW_MODEL, "updateGameScore() called")
                // Calling updateGameScore function
                updateGameScore()

                Log.i(
                    TAG_MAIN_GAME_VIEW_MODEL,
                    "End of the game. Setting the value of the _eventGameFinished as true"
                )
                // Setting the value of the _eventGameFinished variable as true to finish the
                // game
                _eventGameFinished.value = true
            }
            // Case _currentHiraganaLetterRomanization.value is NOT equal to the
            // selectedRomanization
            _currentHiraganaLetterRomanization.value != selectedRomanization -> {
                Log.i(
                    TAG_MAIN_GAME_VIEW_MODEL,
                    "Wrong Answer! Setting the value of the _eventCorrectAnswer as false"
                )
                // Setting the value of the _eventCorrectAnswer variable as false
                _eventCorrectAnswer.value = false

                Log.i(
                    TAG_MAIN_GAME_VIEW_MODEL,
                    "End of the game. Setting the value of the _eventGameFinished as true"
                )
                // Setting the value of the _eventGameFinished variable as true to finish the
                // game
                _eventGameFinished.value = true
            }
        }
    }

    //==========================================================================================
    // generateRadioButtonRomanization function
    //==========================================================================================
    /**
     * Private function that is responsible for generating random romanizations for the radio
     * buttons in the UI.
     *
     * @param correctRomanization - The value of the current romanization for a hiragana letter
     */
    private fun generateRadioButtonRomanization(correctRomanization: String) {
        // Read-only list containing romanization to be used as distractions
        val hiraganaRomanizationList: List<String> = listOf(
            "A", "I", "U", "E", "O", "KA", "KI", "KU", "KE", "KO", "SA", "SHI", "SU", "SE", "SO",
            "TA", "CHI", "TSU", "TE", "TO", "NA", "NI", "NU", "NE", "NO", "HA", "HI", "FU", "HE",
            "HO", "MA", "MI", "MU", "ME", "MO", "YA", "YU", "YO", "RA", "RI", "RU", "RE", "RO",
            "WA", "WI", "WE", "WO", "N"
        )

        // Read-only filtered, shuffled list containing every romanization from the previous list
        // except for the romanization that equals the current correctRomanization
        val filteredList =
            hiraganaRomanizationList.filterNot { it == correctRomanization }.shuffled().apply {
                Log.d(TAG_MAIN_GAME_VIEW_MODEL, "Filtered list size: $size")
            }

        // Variables to used as a index to access a item from the filteredList above.
        // Each variable gets its value from the value returned from the generateRandomNumber
        // function and for the last 3 variables the value passed is the variable above.
        val radioButton1RandomIndexValue = generateRandomNumber(null)
        val radioButton2RandomIndexValue = generateRandomNumber(radioButton1RandomIndexValue)
        val radioButton3RandomIndexValue = generateRandomNumber(radioButton2RandomIndexValue)
        val radioButton4RandomIndexValue = generateRandomNumber(radioButton3RandomIndexValue)

        // Getting a random romanization from the filteredList for each of the
        // radioButtonRomanization variables.
        _radioButton1Romanization.value = filteredList[radioButton1RandomIndexValue]
        _radioButton2Romanization.value = filteredList[radioButton2RandomIndexValue]
        _radioButton3Romanization.value = filteredList[radioButton3RandomIndexValue]
        _radioButton4Romanization.value = filteredList[radioButton4RandomIndexValue]

        // When statement to select which radio button will receive the correct romanization
        // (answer). It does that by generating a random number between 0 and 4 (XOR), and based
        // on this number a radio button will be selected
        when ((0 until 4).random()) {
            // Case 0 - Radio Button 1 selected
            0 -> {
                _radioButton1Romanization.value = correctRomanization
                Log.d(
                    TAG_MAIN_GAME_VIEW_MODEL,
                    "Radio button 1 selected to contain the correct answer"
                )
            }
            // Case 1 - Radio Button 2 selected
            1 -> {
                _radioButton2Romanization.value = correctRomanization
                Log.d(
                    TAG_MAIN_GAME_VIEW_MODEL,
                    "Radio button 2 selected to contain the correct answer"
                )
            }
            // Case 2 - Radio Button 3 selected
            2 -> {
                _radioButton3Romanization.value = correctRomanization
                Log.d(
                    TAG_MAIN_GAME_VIEW_MODEL,
                    "Radio button 3 selected to contain the correct answer"
                )
            }
            // Case 3 - Radio Button 4 selected
            3 -> {
                _radioButton4Romanization.value = correctRomanization
                Log.d(
                    TAG_MAIN_GAME_VIEW_MODEL,
                    "Radio button 4 selected to contain the correct answer"
                )
            }
        }
    }

    //==========================================================================================
    // generateRandomNumber function
    //==========================================================================================
    /**
     * Private function that generates a random number between 0 and 47, to be used a index for
     * accessing a element in the filtered hiragana romanization list in the
     * generateRadioButtonRomanization function.
     *
     * @param previousNumber - Integer that will be filtered from begin a possible generated
     * number
     * @return - Random Integer between 0 and 47 that is not equal to the previousNumber
     * parameter
     */
    private fun generateRandomNumber(previousNumber: Int?): Int {
        return (0 until 47).filterNot {
            it == previousNumber
        }.random()
    }
}