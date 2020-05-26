package com.mathsemilio.hiraganalearner.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mathsemilio.hiraganalearner.data.hiraganaSymbol
import com.mathsemilio.hiraganalearner.data.model.HiraganaSymbol
import com.mathsemilio.hiraganalearner.util.TAG_MAIN_GAME_VIEW_MODEL

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
    // Variable that stores a copy of the hiraganaSymbol list from the data package
    val hiraganaLettersList: MutableList<HiraganaSymbol> = hiraganaSymbol.toMutableList()

    // Variables for storing the last hiragana letter information shuffled from the list
    private var lastHiraganaLetterDrawableId: Int = 0
    private var lastHiraganaLetterRomanization: String? = null

    //==========================================================================================
    // init block
    //==========================================================================================
    init {
        Log.i(TAG_MAIN_GAME_VIEW_MODEL, "Initializing the gameScore value as 0")
        _gameScore.value = 0

        Log.i(TAG_MAIN_GAME_VIEW_MODEL, "Initializing the eventGameFinished value as false")
        _eventGameFinished.value = false

        Log.i(TAG_MAIN_GAME_VIEW_MODEL, "startGame() called")
        startGame()
    }

    //==========================================================================================
    // startGame function
    //==========================================================================================
    private fun startGame() {
        hiraganaLettersList.shuffle()

        _currentHiraganaLetterDrawableId.value = hiraganaLettersList.first().drawableSymbolId
        _currentHiraganaLetterRomanization.value = hiraganaLettersList.first().romanization.also {
            Log.d(TAG_MAIN_GAME_VIEW_MODEL, "First letter romanization: $it")
        }

        lastHiraganaLetterDrawableId = hiraganaLettersList.last().drawableSymbolId
        lastHiraganaLetterRomanization = hiraganaLettersList.last().romanization.also {
            Log.d(TAG_MAIN_GAME_VIEW_MODEL, "Last letter romanization: $it")
        }

        Log.i(TAG_MAIN_GAME_VIEW_MODEL, "generateRadioButtonRomanization() called")
        generateRadioButtonRomanization(_currentHiraganaLetterRomanization.value.toString())
    }

    //==========================================================================================
    // checkUserInput function
    //==========================================================================================
    fun checkUserInput(correctRomanization: String, selectedRomanization: String) {
        when {
            selectedRomanization == correctRomanization -> {
                Log.d(
                    TAG_MAIN_GAME_VIEW_MODEL,
                    "Correct Answer! Setting the value of the _eventGameFinished as true"
                )
                _eventCorrectAnswer.value = true

                Log.i(TAG_MAIN_GAME_VIEW_MODEL, "getNextLetter() called")
                getNextLetter()

                Log.i(TAG_MAIN_GAME_VIEW_MODEL, "generateRadioButtonRomanization() called")
                generateRadioButtonRomanization(_currentHiraganaLetterRomanization.value.toString())

                Log.i(TAG_MAIN_GAME_VIEW_MODEL, "updateGameScore() called")
                updateGameScore()
            }
            selectedRomanization != correctRomanization -> {
                Log.d(
                    TAG_MAIN_GAME_VIEW_MODEL,
                    "Wrong Answer! Setting the value of the _eventGameFinished as false"
                )
                _eventCorrectAnswer.value = false

                Log.i(TAG_MAIN_GAME_VIEW_MODEL, "getNextLetter() called")
                getNextLetter()

                Log.i(TAG_MAIN_GAME_VIEW_MODEL, "generateRadioButtonRomanization() called")
                generateRadioButtonRomanization(_currentHiraganaLetterRomanization.value.toString())
            }
        }
    }

    //==========================================================================================
    // updateGameScore function
    //==========================================================================================
    private fun updateGameScore() {
        _gameScore.value = (_gameScore.value)?.inc().also {
            Log.d(TAG_MAIN_GAME_VIEW_MODEL, "Updated game score: $it")
        }
    }

    //==========================================================================================
    // getNextLetter function
    //==========================================================================================
    private fun getNextLetter() {
        hiraganaLettersList.removeAt(0)
        _currentHiraganaLetterDrawableId.value = hiraganaLettersList.first().drawableSymbolId
        _currentHiraganaLetterRomanization.value = hiraganaLettersList.first().romanization.also {
            Log.d(TAG_MAIN_GAME_VIEW_MODEL, "Next symbol romanization: $it")
        }
    }

    //==========================================================================================
    // getLastLetter function
    //==========================================================================================
    fun getLastLetter(selectedRomanization: String) {
        _currentHiraganaLetterDrawableId.value = lastHiraganaLetterDrawableId
        _currentHiraganaLetterRomanization.value = lastHiraganaLetterRomanization

        when {
            _currentHiraganaLetterRomanization.value == selectedRomanization -> {
                _eventCorrectAnswer.value = true

                updateGameScore()

                _eventGameFinished.value = true
            }
            _currentHiraganaLetterRomanization.value != selectedRomanization -> {
                _eventCorrectAnswer.value = false

                _eventGameFinished.value = true
            }
        }
    }

    //==========================================================================================
    // generateRadioButtonRomanization function
    //==========================================================================================
    private fun generateRadioButtonRomanization(correctRomanization: String) {
        val hiraganaRomanizationList: List<String> = listOf(
            "A", "I", "U", "E", "O", "KA", "KI", "KU", "KE", "KO", "SA", "SHI", "SU", "SE", "SO",
            "TA", "CHI", "TSU", "TE", "TO", "NA", "NI", "NU", "NE", "NO", "HA", "HI", "FU", "HE",
            "HO", "MA", "MI", "MU", "ME", "MO", "YA", "YU", "YO", "RA", "RI", "RU", "RE", "RO",
            "WA", "WI", "WE", "WO", "N"
        )

        val filteredList =
            hiraganaRomanizationList.filterNot { it == correctRomanization }.shuffled().apply {
                Log.d(TAG_MAIN_GAME_VIEW_MODEL, "Filtered list size: $size")
            }

        _radioButton1Romanization.value = filteredList[generateRandomNumber()]
        _radioButton2Romanization.value = filteredList[generateRandomNumber()]
        _radioButton3Romanization.value = filteredList[generateRandomNumber()]
        _radioButton4Romanization.value = filteredList[generateRandomNumber()]

        when ((0 until 4).random()) {
            0 -> {
                _radioButton1Romanization.value = correctRomanization
                Log.d(
                    TAG_MAIN_GAME_VIEW_MODEL,
                    "Radio button 1 selected to contain the correct answer"
                )
            }
            1 -> {
                _radioButton2Romanization.value = correctRomanization
                Log.d(
                    TAG_MAIN_GAME_VIEW_MODEL,
                    "Radio button 2 selected to contain the correct answer"
                )
            }
            2 -> {
                _radioButton3Romanization.value = correctRomanization
                Log.d(
                    TAG_MAIN_GAME_VIEW_MODEL,
                    "Radio button 3 selected to contain the correct answer"
                )
            }
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
    private fun generateRandomNumber(): Int {
        return (0..46).random()
    }
}