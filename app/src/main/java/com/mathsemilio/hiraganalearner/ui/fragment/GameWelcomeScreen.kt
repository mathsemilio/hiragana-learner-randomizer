package com.mathsemilio.hiraganalearner.ui.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.chip.Chip
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.databinding.GameWelcomeScreenBinding
import com.mathsemilio.hiraganalearner.ui.activity.SettingsActivity
import com.mathsemilio.hiraganalearner.util.*

/**
 * Fragment class for game's welcome screen
 */
class GameWelcomeScreen : Fragment() {

    private lateinit var binding: GameWelcomeScreenBinding
    private lateinit var soundPool: SoundPool
    private var soundClick: Int? = null
    private var defaultSharedPreferences: SharedPreferences? = null

    //==========================================================================================
    // onCreateView
    //==========================================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflating the layout with the inflate function from the fragment's binding class
        binding = GameWelcomeScreenBinding.inflate(inflater, container, false)

        defaultSharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireContext())

        configGameDifficultyOptions()

        setupSoundPoolAndLoadSounds()

        binding.appThemeSwitchIcon.setOnClickListener {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO
                    )

                    SharedPreferencesAppTheme(requireContext()).saveThemeValue(APP_THEME_LIGHT_THEME)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES
                    )

                    SharedPreferencesAppTheme(requireContext()).saveThemeValue(APP_THEME_DARK_MODE)
                }
            }
        }

        binding.appConfigIcon.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }

        return binding.root
    }

    //==========================================================================================
    // configGameDifficultyOptions function
    //==========================================================================================
    private fun configGameDifficultyOptions() {
        when (defaultSharedPreferences?.getString(GAME_DIFFICULTY_PREF_KEY, "0")) {
            "0" -> {
                binding.chipGroupGameDifficulty.setOnCheckedChangeListener { group, checkedId ->
                    if (checkedId == -1) {
                        binding.buttonStart.isEnabled = false
                    } else {
                        soundPool.play(soundClick!!, 1F, 1F, 1, 0, 1F)

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
            }
            "1" -> {
                setupUIForDifficultyPreviouslySelected()
                binding.buttonStart.setOnClickListener {
                    val action =
                        GameWelcomeScreenDirections.actionGameWelcomeScreenToMainGameScreen(
                            GAME_DIFFICULTY_VALUE_BEGINNER
                        )

                    findNavController().navigate(action)
                }
            }
            "2" -> {
                setupUIForDifficultyPreviouslySelected()
                binding.buttonStart.setOnClickListener {
                    val action =
                        GameWelcomeScreenDirections.actionGameWelcomeScreenToMainGameScreen(
                            GAME_DIFFICULTY_VALUE_MEDIUM
                        )

                    findNavController().navigate(action)
                }
            }
            "3" -> {
                setupUIForDifficultyPreviouslySelected()
                binding.buttonStart.setOnClickListener {
                    val action =
                        GameWelcomeScreenDirections.actionGameWelcomeScreenToMainGameScreen(
                            GAME_DIFFICULTY_VALUE_HARD
                        )

                    findNavController().navigate(action)
                }
            }
        }
    }

    //==========================================================================================
    // setupSoundPoolAndLoadSounds function
    //==========================================================================================
    private fun setupSoundPoolAndLoadSounds() {
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_GAME)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(2)
            .setAudioAttributes(audioAttributes)
            .build()

        soundClick = soundPool.load(requireContext(), R.raw.brevicep_normal_click, 1)
    }

    //==========================================================================================
    // setupUIForDifficultyPreviouslySelected function
    //==========================================================================================
    private fun setupUIForDifficultyPreviouslySelected() {
        binding.textBodySelectADifficulty.visibility = View.INVISIBLE
        binding.chipGroupGameDifficulty.visibility = View.INVISIBLE
        binding.buttonStart.isEnabled = true
    }
}