package com.mathsemilio.hiraganalearner.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.databinding.GameWelcomeScreenBinding
import com.mathsemilio.hiraganalearner.util.DarkModeSelector

/**
 * Fragment class for game welcome screen
 */

private const val TAG_GAME_WELCOME_SCREEN = "GameWelcomeScreen"

class GameWelcomeScreen : Fragment() {

    //==========================================================================================
    // onCreateView
    //==========================================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Using the inflate function from the Binding class to inflate the layout for this
        // fragment
        val binding: GameWelcomeScreenBinding =
            GameWelcomeScreenBinding.inflate(inflater, container, false)

        binding.darkModeSwitch.isChecked = DarkModeSelector.isActivated

        binding.darkModeSwitch.setOnCheckedChangeListener { buttonView, _ ->
            if (buttonView.isChecked) {
                Log.i(TAG_GAME_WELCOME_SCREEN, "onCreateView: Dark mode on")
                DarkModeSelector.switchDarkModeState()
            } else {
                Log.i(TAG_GAME_WELCOME_SCREEN, "onCreateView: Dark mode off")
                DarkModeSelector.switchDarkModeState()
            }
        }

        // OnClickListener for the Play Button, which calls the navigateToMainGameScreen
        // function
        binding.buttonStart.setOnClickListener { navigateToMainGameScreen() }

        // Returning the root of the inflated layout
        return binding.root
    }

    //==========================================================================================
    // navigateToMainGameScreen function
    //==========================================================================================
    /**
     * Private function that finds the NavController from the activity and calls the navigate
     * function, that receives a action id, to navigate from the welcome screen to main game
     * screen.
     */
    private fun navigateToMainGameScreen() {
        activity?.findNavController(R.id.nav_host_fragment)
            ?.navigate(R.id.action_gameWelcomeScreen_to_mainGameScreen)
    }
}