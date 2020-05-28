package com.mathsemilio.hiraganalearner.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.databinding.MainGameScreenBinding
import com.mathsemilio.hiraganalearner.ui.viewModel.MainGameViewModel
import com.mathsemilio.hiraganalearner.util.TAG_MAIN_GAME_FRAGMENT

/**
 * Fragment class for the main game screen
 */
class MainGameScreen : Fragment() {

    //==========================================================================================
    // Class-wide variables
    //==========================================================================================
    // LateInit variable for the binding class pertaining the layout for this Fragment
    private lateinit var binding: MainGameScreenBinding

    // LateInit variable for the ViewModel pertaining this Fragment
    private lateinit var viewModel: MainGameViewModel

    //==========================================================================================
    // onCreateView
    //==========================================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Using the inflate function from the Binding class to inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.main_game_screen, container, false)

        // Initializing the viewModel variable, and using ViewModelProvider to get a reference of the
        // viewModel class for this fragment
        viewModel = ViewModelProvider(this).get(MainGameViewModel::class.java)

        // Setting the ViewModel for Data Binding - this allows the UI elements in the layout to
        // access the data in the ViewModel directly
        binding.mainGameViewModel = viewModel

        // Setting the current activity as the lifecycle owner of the binding, so that the binding can
        // observe LiveData updates
        binding.lifecycleOwner = this

        // Calling getUserInput()
        getUserInput()

        // Calling observeCurrentHiraganaLetterDrawableId()
        observeCurrentHiraganaLetterDrawableId()

        // Calling observeEventCorrectAnswer()
        observeEventCorrectAnswer()

        // Calling observeEventGameFinished()
        observeEventGameFinished()

        // OnClickListener for the exit button, which calls the navigateToWelcomeScreen function
        binding.buttonExit.setOnClickListener { navigateToWelcomeScreen() }

        // Returning the root of the inflated layout
        return binding.root
    }

    //==========================================================================================
    // getUserInput function
    //==========================================================================================
    /**
     * Private function that is responsible for getting the user input from the UI and sending the data
     * to the ViewModel.
     */
    private fun getUserInput() {
        // OnCheckedChangeListener to determine if any of the Radio Buttons in the group is checked
        // or not
        binding.radioGroupHiraganaLetters.setOnCheckedChangeListener { group, checkedId ->
            // OnClickListener for the Verify Answer button
            binding.buttonVerifyAnswer.setOnClickListener {
                // Checking if checkedRadioButtonId returns -1, meaning none of the button in the
                // group is checked
                if (group.checkedRadioButtonId == -1) {
                    // Building a Toast to ask the user to select a Radio Button
                    Log.i(
                        TAG_MAIN_GAME_FRAGMENT,
                        "Building the Toast to ask the user to select a option"
                    )
                    Toast.makeText(
                        activity,
                        R.string.toast_msg_please_select_a_option,
                        Toast.LENGTH_SHORT
                    ).show()
                    // Else, there is a button selected in the group
                } else {
                    // Getting the checked radio button
                    val radioButton: RadioButton = group.findViewById(checkedId)
                    // Getting the text from the checked radio button
                    val selectedRomanization: String = radioButton.text.toString().also {
                        Log.d(TAG_MAIN_GAME_FRAGMENT, "Text of the selected radio button: $it")
                    }

                    // Checking if hiraganaLettersList size from the ViewModel equals 1
                    // (the last letter in the list), in this case the getLastLetter is called and
                    // the selected radio button text is passed to the function
                    if (viewModel.hiraganaLettersList.size == 1) {
                        Log.i(TAG_MAIN_GAME_FRAGMENT, "Last letter from the hiraganaLettersList")
                        viewModel.getLastLetter(selectedRomanization)
                    } else {
                        // Else the checkUserInput is called and the value of the currentHiraganaLetterRomanization
                        // from the ViewModel (the correct answer for the current letter on the screen),
                        // and the selectedRomanization is passed
                        Log.i(TAG_MAIN_GAME_FRAGMENT, "Calling checkUserInput()")
                        viewModel.checkUserInput(
                            viewModel.currentHiraganaLetterRomanization.value.toString(),
                            selectedRomanization
                        )
                    }
                }
            }
        }
    }

    //==========================================================================================
    // observeCurrentHiraganaLetterDrawableId function
    //==========================================================================================
    /**
     * Private function that is responsible for observing the value of the currentHiraganaLetterDrawableId
     * LiveData from the ViewModel.
     */
    private fun observeCurrentHiraganaLetterDrawableId() {
        viewModel.currentHiraganaLetterDrawableId.observe(
            viewLifecycleOwner,
            Observer { drawableSymbolId ->
                // Calling the setImageResource function and passing the drawableSymbolId as the
                // resource ID of the vector to be drawn on the imageHiraganaLetterMainScreen ImageView
                binding.imageHiraganaLetterMainGameScreen.setImageResource(drawableSymbolId)
            })
    }

    //==========================================================================================
    // observeEventCorrectAnswer function
    //==========================================================================================
    /**
     * Private function that is responsible for observing the value of the eventCorrectAnswer
     * LiveData from the ViewModel.
     */
    private fun observeEventCorrectAnswer() {
        viewModel.eventCorrectAnswer.observe(viewLifecycleOwner, Observer { correctAnswer ->
            // When statement to check the the value of the eventCorrectAnswer variable
            when (correctAnswer) {
                // Case true - Correct Answer
                true -> {
                    // Calling buildAlertDialog to build a dialog alerting the user that his answer
                    // is correct
                    Log.i(
                        TAG_MAIN_GAME_FRAGMENT,
                        "Building the alert dialog alerting the user that his answer is correct"
                    )
                    buildAlertDialog(
                        R.string.alertDialogCorrectAnswer_title,
                        getString(R.string.alertDialogCorrectAnswer_msg),
                        R.string.alertDialogCorrectAnswer_positive_button_text
                    )
                    // Clearing the checked button from the RadioGroup
                    binding.radioGroupHiraganaLetters.clearCheck()
                }
                false -> {
                    // Calling buildAlertDialog to build a dialog alerting the user that his answer
                    // is incorrect
                    Log.i(
                        TAG_MAIN_GAME_FRAGMENT,
                        "Building the alert dialog alerting the user that his answer is incorrect"
                    )
                    buildAlertDialog(
                        R.string.alertDialogWrongAnswer_title,
                        getString(
                            R.string.alertDialogWrongAnswer_msg,
                            viewModel.currentHiraganaLetterRomanization.value
                        ),
                        R.string.alertDialogWrongAnswer_positive_button_text
                    )
                    // Clearing the checked button from the RadioGroup
                    binding.radioGroupHiraganaLetters.clearCheck()
                }
            }
        })
    }

    //==========================================================================================
    // observeEventCorrectAnswer function
    //==========================================================================================
    /**
     * Private function that is responsible for observing the value of the eventGameFinished
     * LiveData from the ViewModel.
     */
    private fun observeEventGameFinished() {
        viewModel.eventGameFinished.observe(viewLifecycleOwner, Observer { gameFinished ->
            // Checking if the eventGameFinished value is true
            if (gameFinished) {
                // Variable that calls the actionMainGameScreenToGameScoreScreen function from the
                // MainGameScreen generated class, and passing the game score LiveData value from
                // the viewModel (with SafeArgs)
                val action = MainGameScreenDirections.actionMainGameScreenToGameScoreScreen(
                    viewModel.gameScore.value!!.toInt()
                )
                // Passing the action for navigating to the score screen
                activity?.findNavController(R.id.nav_host_fragment)?.navigate(action)
            }
        })
    }

    //==========================================================================================
    // navigateToWelcomeScreen function
    //==========================================================================================
    /**
     * Private function that finds the NavController from the activity and calls the navigate function,
     * that receives a action id, to navigate from the main game screen to welcome screen.
     */
    private fun navigateToWelcomeScreen() {
        activity?.findNavController(R.id.nav_host_fragment)
            ?.navigate(R.id.action_mainGameScreen_to_gameWelcomeScreen)
    }

    //==========================================================================================
    // buildAlertDialog function
    //==========================================================================================
    /**
     * Private function that builds a AlertDialog for alerting the user about certain game events.
     *
     * @param title - Receives a integer that corresponds as the resource ID for the dialog title
     * @param message - Receives a string pertaining the dialog message
     * @param positiveButtonText - Receives a integer that corresponds as the resource ID for the dialog
     * positive button text
     */
    private fun buildAlertDialog(title: Int, message: String, positiveButtonText: Int) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveButtonText, null)
        builder.show()
    }
}