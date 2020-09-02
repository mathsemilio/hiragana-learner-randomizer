package com.mathsemilio.hiraganalearner.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.databinding.MainGameScreenBinding
import com.mathsemilio.hiraganalearner.ui.viewModel.MainGameViewModel
import com.mathsemilio.hiraganalearner.ui.viewModel.MainGameViewModelFactory
import com.mathsemilio.hiraganalearner.util.*

/**
 * Fragment class for the main game screen
 */
class MainGameScreen : Fragment() {

    private lateinit var binding: MainGameScreenBinding
    private lateinit var viewModelFactory: MainGameViewModelFactory
    private lateinit var viewModel: MainGameViewModel
    private var gameDifficultyValue: Int? = null
    private var isRestored = false

    //==========================================================================================
    // onCreateView
    //==========================================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Using the inflate function from the DataBindingUtil class to inflate the layout for
        // this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.main_game_screen, container, false)

        gameDifficultyValue =
            MainGameScreenArgs.fromBundle(requireArguments()).gameDifficultyValue

        viewModelFactory = MainGameViewModelFactory(gameDifficultyValue!!)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainGameViewModel::class.java)

        // Setting the ViewModel for Data Binding - this allows the UI elements in the layout to
        // access the data in the ViewModel directly
        binding.mainGameViewModel = viewModel

        // Setting the current activity as the lifecycle owner of the binding, so that the binding
        // can observe LiveData updates
        binding.lifecycleOwner = this

        subscribeToObservers()

        binding.textBodyGameDifficulty.text =
            getGameDifficultyStringBasedOnTheDifficultyValue(gameDifficultyValue!!)

        /*
        Listener for the chipGroupRomaniztionOptions chip group to enable or disable the
        buttonVerifyAnswer button.
        */
        binding.chipGroupRomaniztionOptions.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == -1) {
                binding.buttonVerifyAnswer.isEnabled = false
            } else {
                binding.buttonVerifyAnswer.isEnabled = true

                // Getting the text from the selected chip
                val selectedChip = group.findViewById<Chip>(checkedId)
                val selectedRomanization: String = selectedChip.text.toString()

                // Listener for the buttonVerifyAnswer button
                binding.buttonVerifyAnswer.setOnClickListener {
                    viewModel.countDownTimer?.cancel()

                    /*
                    Checking the hiraganaLettersList size, if equals 1, the getLastLetter
                    function is called, else checkUserInput is called.
                    */
                    if (viewModel.hiraganaLettersList.size == 1) {
                        viewModel.getLastLetter(selectedRomanization)
                    } else {
                        viewModel.checkUserInput(selectedRomanization)
                    }
                }
            }
        }

        binding.buttonExit.setOnClickListener { navigateToWelcomeScreen() }

        // Returning the root of the inflated layout
        return binding.root
    }

    //==========================================================================================
    // setGameDifficultyStringBasedOnTheDifficultyValue function
    //==========================================================================================
    /**
     * Function that based on the game difficulty value, returns a String that corresponds the
     * game difficulty value.
     */
    private fun getGameDifficultyStringBasedOnTheDifficultyValue(gameDifficultyValue: Int): String {
        return when (gameDifficultyValue) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> getString(R.string.game_difficulty_beginner)
            GAME_DIFFICULTY_VALUE_MEDIUM -> getString(R.string.game_difficulty_medium)
            else -> getString(R.string.game_difficulty_hard)
        }
    }

    //==========================================================================================
    // getGameTimeRemainingDefaultValue function
    //==========================================================================================
    private fun getGameTimeRemainingDefaultValue(gameDifficultyValue: Int): Long {
        return when (gameDifficultyValue) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> COUNTDOWN_TIME_BEGINNER
            GAME_DIFFICULTY_VALUE_MEDIUM -> COUNTDOWN_TIME_MEDIUM
            else -> COUNTDOWN_TIME_HARD
        }
    }

    //==========================================================================================
    // subscribeToObservers function
    //==========================================================================================
    /**
     * Function that subscribes (attaches) the observers to each LiveData variables to be
     * observed.
     */
    private fun subscribeToObservers() {
        /*
        Observing the eventCorrectAnswer to show an alert dialog based on if the user's answer
        is correct or not.
        */
        viewModel.eventCorrectAnswer.observe(viewLifecycleOwner, Observer { answerIsCorrect ->
            if (answerIsCorrect == true) {
                buildAlertDialog(
                    R.string.alertDialogCorrectAnswer_title,
                    getString(R.string.alertDialogCorrectAnswer_msg),
                    R.string.alertDialogCorrectAnswer_positive_button_text,
                    DialogInterface.OnClickListener { _, _ ->
                        /*
                        Checking the eventGameFinished value, if it's true, the
                        navigateToScoreScreen is called, else, the getNextLetter function is
                        called and the radioGroupHiraganaLetters radio group is cleared.
                        */
                        if (viewModel.eventGameFinished.value == true) {
                            navigateToScoreScreen(viewModel.gameScore.value!!)
                        } else {
                            binding.chipGroupRomaniztionOptions.clearCheck()
                            viewModel.getNextLetter()
                        }
                    })
            } else {
                // Building an AlertDialog to alert the user that his answer is incorrect
                buildAlertDialog(
                    R.string.alertDialogWrongAnswer_title,
                    getString(
                        R.string.alertDialogWrongAnswer_msg,
                        viewModel.currentHiraganaLetterRomanization.value
                    ),
                    R.string.alertDialogWrongAnswer_positive_button_text,
                    DialogInterface.OnClickListener { _, _ ->
                        if (viewModel.eventGameFinished.value == true) {
                            navigateToScoreScreen(viewModel.gameScore.value!!)
                        } else {
                            binding.chipGroupRomaniztionOptions.clearCheck()
                            viewModel.getNextLetter()
                        }
                    })
            }
        })

        viewModel.eventTimeOver.observe(viewLifecycleOwner, Observer { timeIsOver ->
            if (timeIsOver) {
                buildAlertDialog(
                    R.string.alertDialogTimeOver_title,
                    getString(
                        R.string.alertDialogTimeOver_msg,
                        viewModel.currentHiraganaLetterRomanization.value
                    ),
                    R.string.alertDialogTimeOver_positive_button_text,
                    DialogInterface.OnClickListener { _, _ ->
                        if (viewModel.eventGameFinished.value == true) {
                            navigateToScoreScreen(viewModel.gameScore.value!!)
                        } else {
                            binding.chipGroupRomaniztionOptions.clearCheck()
                            viewModel.getNextLetter()
                        }
                    })
            }
        })
    }

    //==========================================================================================
    // navigateToWelcomeScreen function
    //==========================================================================================
    /**
     * Function to navigate from the current screen to the welcome screen.
     */
    private fun navigateToWelcomeScreen() {
        findNavController().navigate(R.id.action_mainGameScreen_to_gameWelcomeScreen)
    }

    //==========================================================================================
    // navigateToScoreScreen function
    //==========================================================================================
    /**
     * Function to navigate from the current screen to the score screen, while passing the game's
     * score to the destination.
     *
     * @param gameScore - Integer for the game score to be passed to the score fragment
     */
    private fun navigateToScoreScreen(gameScore: Int) {
        val action = MainGameScreenDirections.actionMainGameScreenToGameScoreScreen(gameScore)
        findNavController().navigate(action)
    }

    //==========================================================================================
    // buildAlertDialog function
    //==========================================================================================
    /**
     * Function to build an alert dialog to alert the user about certain game events.
     *
     * @param title - Integer for title's resource id
     * @param message - String for the dialog's message
     * @param positiveButtonText - Integer for the dialog's positive button resource id
     * @param listener - DialogInterface.OnClickListener for listening when the positive button
     * is clicked
     */
    private fun buildAlertDialog(
        title: Int, message: String, positiveButtonText: Int,
        listener: DialogInterface.OnClickListener
    ) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(positiveButtonText, listener)
            setCancelable(false)
            show()
        }
    }

    //==========================================================================================
    // Fragment lifecycle callbacks
    //==========================================================================================
    override fun onPause() {
        super.onPause()
        isRestored = true

        viewModel.countDownTimer?.cancel()

        SharedPreferencesRemainingGameTime(requireContext()).saveRemainingGameTime(
            viewModel.currentGameTime.value!!
        )
    }

    override fun onResume() {
        super.onResume()
        if (isRestored) {
            val gameTimeRemaining =
                SharedPreferencesRemainingGameTime(requireContext()).retrieveGameTimeRemaining(
                    getGameTimeRemainingDefaultValue(gameDifficultyValue!!)
                )

            viewModel.setupGameTimer(gameTimeRemaining.times(ONE_SECOND))
        }
    }
}