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

class GameWelcomeScreen : Fragment() {

    //==========================================================================================
    // Class-wide variables
    //==========================================================================================
    private lateinit var binding: GameWelcomeScreenBinding

    //==========================================================================================
    // onCreateView
    //==========================================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = GameWelcomeScreenBinding.inflate(inflater, container, false)

        binding.buttonPlay.setOnClickListener {
            Log.i(TAG_GAME_WELCOME_SCREEN, "navigateToMainGameScreen() called")
            navigateToMainGameScreen()
        }

        return binding.root
    }

    //==========================================================================================
    // navigateToMainGameScreen function
    //==========================================================================================
    private fun navigateToMainGameScreen() {
        activity?.findNavController(R.id.nav_host_fragment)
            ?.navigate(R.id.action_gameWelcomeScreen_to_mainGameScreen)
    }
}