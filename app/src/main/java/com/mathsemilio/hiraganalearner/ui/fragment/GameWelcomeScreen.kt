package com.mathsemilio.hiraganalearner.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.databinding.GameWelcomeScreenBinding
import com.mathsemilio.hiraganalearner.ui.activity.SettingsActivity
import com.mathsemilio.hiraganalearner.util.GAME_DIFFICULTY_VALUE_BEGINNER
import com.mathsemilio.hiraganalearner.util.GAME_DIFFICULTY_VALUE_HARD
import com.mathsemilio.hiraganalearner.util.GAME_DIFFICULTY_VALUE_MEDIUM

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

        binding.appConfigIcon.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }

        binding.chipGroupGameDifficulty.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == -1) {
                binding.buttonStart.isEnabled = false
            } else {
                binding.buttonStart.isEnabled = true

                val checkedRadioButton = group.findViewById<Chip>(checkedId)

                val gameDifficultyValue = when (checkedRadioButton.text.toString()) {
                    getString(R.string.game_difficulty_beginner) -> GAME_DIFFICULTY_VALUE_BEGINNER
                    getString(R.string.game_difficulty_medium) -> GAME_DIFFICULTY_VALUE_MEDIUM
                    else -> GAME_DIFFICULTY_VALUE_HARD
                }

                binding.buttonStart.setOnClickListener {
                    binding.chipGroupGameDifficulty.clearCheck()
                    val action =
                        GameWelcomeScreenDirections.actionGameWelcomeScreenToMainGameScreen(
                            gameDifficultyValue
                        )
                    findNavController().navigate(action)
                }
            }
        }

        return binding.root
    }
}