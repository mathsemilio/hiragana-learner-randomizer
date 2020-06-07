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

private const val TAG_GAME_WELCOME_SCREEN = "GameWelcomeScreen"

/**
 * Fragment class for game's welcome screen
 */
class GameWelcomeScreen : Fragment() {

    //==========================================================================================
    // onCreateView
    //==========================================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflating the layout with the inflate function from the fragment's binding class
        val binding: GameWelcomeScreenBinding =
            GameWelcomeScreenBinding.inflate(inflater, container, false)

        // Listener for the buttonStart button
        binding.buttonStart.setOnClickListener { navigateToMainGameScreen() }

        /*
        Setting the state of the darkModeSwitch switch according to value returned by the
        checkDarkModeSystemStatus function
        */
        binding.darkModeSwitch.isChecked = DarkModeSelector.checkDarkModeSystemStatus()

        // Setting the state of the darkModeSwitch switch according to the isActivated value
        binding.darkModeSwitch.setOnCheckedChangeListener { buttonView, _ ->
            if (buttonView.isChecked) {
                Log.i(TAG_GAME_WELCOME_SCREEN, "onCreateView: Dark mode on")
                DarkModeSelector.switchDarkModeState()
            } else {
                Log.i(TAG_GAME_WELCOME_SCREEN, "onCreateView: Dark mode off")
                DarkModeSelector.switchDarkModeState()
            }
        }

        // Returning the root of the inflated layout
        return binding.root
    }

    //==========================================================================================
    // navigateToMainGameScreen function
    //==========================================================================================
    /**
     * Function for navigating from the current screen to the main game screen.
     */
    private fun navigateToMainGameScreen() {
        activity?.findNavController(R.id.nav_host_fragment)
            ?.navigate(R.id.action_gameWelcomeScreen_to_mainGameScreen)
    }
}