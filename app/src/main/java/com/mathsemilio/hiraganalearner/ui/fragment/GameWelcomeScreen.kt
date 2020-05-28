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
import com.mathsemilio.hiraganalearner.util.TAG_GAME_WELCOME_SCREEN

/**
 * Fragment class for game welcome screen
 */
class GameWelcomeScreen : Fragment() {

    //==========================================================================================
    // Class-wide variables
    //==========================================================================================
    // LateInit variable for the binding class pertaining the layout for this Fragment
    private lateinit var binding: GameWelcomeScreenBinding

    //==========================================================================================
    // onCreateView
    //==========================================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Using the inflate function from the Binding class to inflate the layout for this fragment
        binding = GameWelcomeScreenBinding.inflate(inflater, container, false)

        // OnClickListener for the Play Button, which calls the navigateToMainGameScreen function
        binding.buttonPlay.setOnClickListener {
            Log.i(TAG_GAME_WELCOME_SCREEN, "navigateToMainGameScreen() called")
            navigateToMainGameScreen()
        }

        // Returning the root of the inflated layout
        return binding.root
    }

    //==========================================================================================
    // navigateToMainGameScreen function
    //==========================================================================================
    /**
     * Private function that finds the NavController from the activity and calls the navigate function,
     * that receives a action id, to navigate from the welcome screen to main game screen.
     */
    private fun navigateToMainGameScreen() {
        activity?.findNavController(R.id.nav_host_fragment)
            ?.navigate(R.id.action_gameWelcomeScreen_to_mainGameScreen)
    }
}