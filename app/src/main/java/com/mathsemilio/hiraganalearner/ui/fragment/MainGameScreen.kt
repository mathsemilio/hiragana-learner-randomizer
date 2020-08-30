package com.mathsemilio.hiraganalearner.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.databinding.MainGameScreenBinding
import com.mathsemilio.hiraganalearner.ui.viewModel.MainGameViewModel

/**
 * Fragment class for the main game screen
 */
class MainGameScreen : Fragment() {

    //==========================================================================================
    // Class-wide variables
    //==========================================================================================
    private lateinit var binding: MainGameScreenBinding
    private val viewModel by viewModels<MainGameViewModel>()

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

        // Setting the ViewModel for Data Binding - this allows the UI elements in the layout to
        // access the data in the ViewModel directly
        binding.mainGameViewModel = viewModel

        // Setting the current activity as the lifecycle owner of the binding, so that the binding
        // can observe LiveData updates
        binding.lifecycleOwner = this

        /*
        Listener for the radioGroupHiraganaLetters radio group to enable or disable the
        buttonVerifyAnswer button
        */
        binding.radioGroupHiraganaLetters.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == -1) {
                binding.buttonVerifyAnswer.isEnabled = false
            } else {
                binding.buttonVerifyAnswer.isEnabled = true

                // Getting the text from the checked radio button
                val radioButton: RadioButton = group.findViewById(checkedId)
                val selectedRomanization: String = radioButton.text.toString()

                // Listener for the buttonVerifyAnswer button
                binding.buttonVerifyAnswer.setOnClickListener {
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

        // Listener for the buttonExit button
        binding.buttonExit.setOnClickListener { navigateToWelcomeScreen() }

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
                            navigateToScoreScreen(viewModel.gameScore.value!!.toInt())
                        } else {
                            binding.radioGroupHiraganaLetters.clearCheck()
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
                            navigateToScoreScreen(viewModel.gameScore.value!!.toInt())
                        } else {
                            binding.radioGroupHiraganaLetters.clearCheck()
                            viewModel.getNextLetter()
                        }
                    })
            }
        })

        // Returning the root of the inflated layout
        return binding.root
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
}