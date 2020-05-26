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

class MainGameScreen : Fragment() {

    //==========================================================================================
    // Class-wide variables
    //==========================================================================================
    private lateinit var binding: MainGameScreenBinding
    private lateinit var viewModel: MainGameViewModel

    //==========================================================================================
    // onCreateView
    //==========================================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.main_game_screen, container, false)

        viewModel = ViewModelProvider(this).get(MainGameViewModel::class.java)

        binding.mainGameViewModel = viewModel

        binding.lifecycleOwner = this

        Log.i(TAG_MAIN_GAME_FRAGMENT, "getUserInput() called")
        getUserInput()

        observeCurrentHiraganaLetterDrawableId()

        observeEventCorrectAnswer()

        observeEventGameFinished()

        binding.buttonExit.setOnClickListener { navigateToWelcomeScreen() }

        return binding.root
    }

    //==========================================================================================
    // getUserInput function
    //==========================================================================================
    private fun getUserInput() {
        binding.radioGroupHiraganaLetters.setOnCheckedChangeListener { group, checkedId ->
            binding.buttonVerifyAnswer.setOnClickListener {
                if (group.checkedRadioButtonId == -1) {
                    Toast.makeText(
                        activity,
                        R.string.toast_msg_please_select_a_option,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val radioButton: RadioButton = group.findViewById(checkedId)
                    val selectedRomanization: String = radioButton.text.toString()

                    if (viewModel.hiraganaLettersList.size == 1) {
                        viewModel.getLastLetter(selectedRomanization)
                    } else {
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
    private fun observeCurrentHiraganaLetterDrawableId() {
        viewModel.currentHiraganaLetterDrawableId.observe(
            viewLifecycleOwner,
            Observer { drawableSymbolId ->
                binding.imageHiraganaLetterMainGameScreen.setImageResource(drawableSymbolId)
            })
    }

    //==========================================================================================
    // observeEventCorrectAnswer function
    //==========================================================================================
    private fun observeEventCorrectAnswer() {
        viewModel.eventCorrectAnswer.observe(viewLifecycleOwner, Observer { correctAnswer ->
            when (correctAnswer) {
                true -> {
                    buildAlertDialog(
                        R.string.alertDialogCorrectAnswer_title,
                        getString(R.string.alertDialogCorrectAnswer_msg),
                        R.string.alertDialogCorrectAnswer_positive_button_text
                    )
                    binding.radioGroupHiraganaLetters.clearCheck()
                }
                false -> {
                    buildAlertDialog(
                        R.string.alertDialogWrongAnswer_title,
                        getString(
                            R.string.alertDialogWrongAnswer_msg,
                            viewModel.currentHiraganaLetterRomanization.value
                        ),
                        R.string.alertDialogWrongAnswer_positive_button_text
                    )
                    binding.radioGroupHiraganaLetters.clearCheck()
                }
            }
        })
    }

    //==========================================================================================
    // observeEventCorrectAnswer function
    //==========================================================================================
    private fun observeEventGameFinished() {
        viewModel.eventGameFinished.observe(viewLifecycleOwner, Observer { gameFinished ->
            if (gameFinished) {
                val action = MainGameScreenDirections.actionMainGameScreenToGameScoreScreen(
                    viewModel.gameScore.value!!.toInt()
                )
                activity?.findNavController(R.id.nav_host_fragment)?.navigate(action)
            }
        })
    }

    private fun navigateToWelcomeScreen() {
        activity?.findNavController(R.id.nav_host_fragment)
            ?.navigate(R.id.action_mainGameScreen_to_gameWelcomeScreen)
    }

    //==========================================================================================
    // buildAlertDialog function
    //==========================================================================================
    private fun buildAlertDialog(title: Int, message: String, positiveButtonText: Int) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveButtonText, null)
        builder.show()
    }
}