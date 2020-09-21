package com.mathsemilio.hiraganalearner.ui.fragment

import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.chip.Chip
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.databinding.GameWelcomeScreenBinding
import com.mathsemilio.hiraganalearner.others.GAME_DIFFICULTY_VALUE_BEGINNER
import com.mathsemilio.hiraganalearner.others.GAME_DIFFICULTY_VALUE_HARD
import com.mathsemilio.hiraganalearner.others.GAME_DIFFICULTY_VALUE_MEDIUM
import com.mathsemilio.hiraganalearner.others.SOUND_EFFECTS_VOLUME_PREF_KEY

/**
 * Fragment class for game's welcome screen
 */
class GameWelcomeScreen : Fragment() {

    companion object {
        const val GAME_DIFFICULTY = "gameDifficulty"
    }

    private lateinit var binding: GameWelcomeScreenBinding
    private lateinit var defaultSharedPreferences: SharedPreferences
    private lateinit var soundPool: SoundPool
    private var soundEffectsVolume = 0f
    private var soundButtonClick = 0
    private var soundClick = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = GameWelcomeScreenBinding.inflate(inflater, container, false)

        defaultSharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireContext())

        soundEffectsVolume =
            defaultSharedPreferences.getInt(SOUND_EFFECTS_VOLUME_PREF_KEY, 0).toFloat().div(10f)

        configGameDifficultyOptions()

        setupSoundPoolAndLoadSounds()

        binding.imageViewAppConfigIcon.setOnClickListener {
            findNavController().navigate(R.id.action_gameWelcomeScreen_to_settingsFragment)
        }

        return binding.root
    }

    /**
     * Configures and sets up based on the game difficulty value from the default
     * Shared Preferences.
     */
    private fun configGameDifficultyOptions() {

        /**
         * Hides the chip group and enables the Start button
         */
        fun setupUIForDifficultyPreviouslySelected() {
            binding.textBodySelectADifficulty.visibility = View.INVISIBLE
            binding.chipGroupGameDifficulty.visibility = View.INVISIBLE
            binding.buttonStart.isEnabled = true
        }

        when (defaultSharedPreferences.getString(GAME_DIFFICULTY, "0")) {
            "0" -> {
                binding.chipGroupGameDifficulty.setOnCheckedChangeListener { group, checkedId ->
                    if (checkedId == -1) {
                        binding.buttonStart.isEnabled = false
                    } else {
                        soundPool.play(soundClick, soundEffectsVolume, soundEffectsVolume, 1, 0, 1F)

                        binding.buttonStart.isEnabled = true

                        val checkedRadioButton = group.findViewById<Chip>(checkedId)

                        val gameDifficultyValue = when (checkedRadioButton.text.toString()) {
                            getString(R.string.game_difficulty_beginner) -> GAME_DIFFICULTY_VALUE_BEGINNER
                            getString(R.string.game_difficulty_medium) -> GAME_DIFFICULTY_VALUE_MEDIUM
                            else -> GAME_DIFFICULTY_VALUE_HARD
                        }

                        binding.buttonStart.setOnClickListener {
                            soundPool.play(
                                soundButtonClick,
                                soundEffectsVolume,
                                soundEffectsVolume,
                                1,
                                0,
                                1F
                            )

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

                binding.textBodyOnGameDifficulty.apply {
                    visibility = View.VISIBLE
                    text = getString(
                        R.string.on_game_difficulty,
                        getString(R.string.game_difficulty_beginner)
                    )
                }

                binding.buttonStart.setOnClickListener {
                    soundPool.play(
                        soundButtonClick,
                        soundEffectsVolume,
                        soundEffectsVolume,
                        0,
                        0,
                        1F
                    )

                    val action =
                        GameWelcomeScreenDirections.actionGameWelcomeScreenToMainGameScreen(
                            GAME_DIFFICULTY_VALUE_BEGINNER
                        )

                    findNavController().navigate(action)
                }
            }
            "2" -> {
                setupUIForDifficultyPreviouslySelected()

                binding.textBodyOnGameDifficulty.apply {
                    visibility = View.VISIBLE
                    text = getString(
                        R.string.on_game_difficulty,
                        getString(R.string.game_difficulty_medium)
                    )
                }

                binding.buttonStart.setOnClickListener {
                    soundPool.play(
                        soundButtonClick,
                        soundEffectsVolume,
                        soundEffectsVolume,
                        0,
                        0,
                        1F
                    )

                    val action =
                        GameWelcomeScreenDirections.actionGameWelcomeScreenToMainGameScreen(
                            GAME_DIFFICULTY_VALUE_MEDIUM
                        )

                    findNavController().navigate(action)
                }
            }
            "3" -> {
                setupUIForDifficultyPreviouslySelected()

                binding.textBodyOnGameDifficulty.apply {
                    visibility = View.VISIBLE
                    text = getString(
                        R.string.on_game_difficulty,
                        getString(R.string.game_difficulty_hard)
                    )
                }

                binding.buttonStart.setOnClickListener {
                    soundPool.play(
                        soundButtonClick,
                        soundEffectsVolume,
                        soundEffectsVolume,
                        0,
                        0,
                        1F
                    )

                    val action =
                        GameWelcomeScreenDirections.actionGameWelcomeScreenToMainGameScreen(
                            GAME_DIFFICULTY_VALUE_HARD
                        )

                    findNavController().navigate(action)
                }
            }
        }
    }

    /**
     * Sets up Sound Pool and loads the sounds to be used in this fragment.
     */
    private fun setupSoundPoolAndLoadSounds() {
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_GAME)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        soundButtonClick = soundPool.load(requireContext(), R.raw.jaoreir_button_simple_01, 1)
        soundClick = soundPool.load(requireContext(), R.raw.brandondelehoy_series_of_clicks, 1)
    }
}