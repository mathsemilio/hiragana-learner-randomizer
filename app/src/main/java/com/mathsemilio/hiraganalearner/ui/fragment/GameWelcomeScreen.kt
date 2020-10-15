package com.mathsemilio.hiraganalearner.ui.fragment

import android.content.SharedPreferences
import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.chip.Chip
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.databinding.GameWelcomeScreenBinding
import com.mathsemilio.hiraganalearner.others.*

/**
 * Fragment class for game's welcome screen
 */
class GameWelcomeScreen : Fragment() {

    companion object {
        const val SHOW_OPTIONS = "0"
        const val DEFAULT_DIFFICULTY_EASY = "1"
        const val DEFAULT_DIFFICULTY_MEDIUM = "2"
        const val DEFAULT_DIFFICULTY_HARD = "3"
    }

    private var _binding: GameWelcomeScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var defaultSharedPreferences: SharedPreferences
    private var soundPool: SoundPool? = null
    private lateinit var interstitialAd: InterstitialAd
    private var gameDifficultyValue = 0
    private var soundEffectsEnabled = true
    private var soundEffectsVolume = 0f
    private var soundButtonClick = 0
    private var soundClick = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = GameWelcomeScreenBinding.inflate(inflater, container, false)

        initializeVariables()

        setupInterstitialAd()

        configureGameDifficultyOptions()

        binding.imageViewAppConfigIcon.setOnClickListener {
            findNavController().navigate(R.id.action_gameWelcomeScreen_to_settingsFragment)
        }

        return binding.root
    }

    private fun initializeVariables() {
        defaultSharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireContext())

        soundEffectsVolume =
            defaultSharedPreferences.getInt(SOUND_EFFECTS_VOLUME_PREF_KEY, 0).toFloat().div(10f)

        if (soundEffectsVolume == 0f) {
            soundEffectsEnabled = false
        } else {
            soundPool = setupSoundPool(1)
            loadSoundEffects()
        }
    }

    private fun setupInterstitialAd() {
        interstitialAd = InterstitialAd(requireContext()).apply {
            adUnitId = getString(R.string.interstitialAdUnitId)
            adListener = (object : AdListener() {
                override fun onAdClosed() {
                    startGame(gameDifficultyValue)
                }
            })
            loadAd(AdRequest.Builder().build())
        }
    }

    private fun startGame(gameDifficultyValue: Int) {
        findNavController().navigate(
            GameWelcomeScreenDirections.actionGameWelcomeScreenToMainGameScreen(gameDifficultyValue)
        )
    }

    private fun loadAdAndStartGame(gameDifficultyValue: Int) {
        if (interstitialAd.isLoaded) interstitialAd.show() else startGame(gameDifficultyValue)
    }

    private fun configureGameDifficultyOptions() {

        fun setupUIForDifficultyPreviouslySelected() {
            binding.textBodySelectADifficulty.visibility = View.INVISIBLE
            binding.chipGroupGameDifficulty.visibility = View.INVISIBLE
            binding.buttonStart.isEnabled = true
        }

        when (defaultSharedPreferences.getString(DEFAULT_GAME_DIFFICULTY_PREF_KEY, "0")) {
            SHOW_OPTIONS -> {
                binding.chipGroupGameDifficulty.setOnCheckedChangeListener { group, checkedId ->
                    if (checkedId == -1) {
                        binding.buttonStart.isEnabled = false
                    } else {
                        if (soundEffectsEnabled)
                            soundPool?.play(
                                soundClick,
                                soundEffectsVolume,
                                soundEffectsVolume,
                                1,
                                0,
                                1F
                            )

                        binding.buttonStart.isEnabled = true

                        val checkedRadioButton = group.findViewById<Chip>(checkedId)

                        gameDifficultyValue = when (checkedRadioButton.text.toString()) {
                            getString(R.string.game_difficulty_beginner) -> GAME_DIFFICULTY_VALUE_BEGINNER
                            getString(R.string.game_difficulty_medium) -> GAME_DIFFICULTY_VALUE_MEDIUM
                            else -> GAME_DIFFICULTY_VALUE_HARD
                        }

                        binding.buttonStart.setOnClickListener {
                            if (soundEffectsEnabled)
                                soundPool?.play(
                                    soundButtonClick,
                                    soundEffectsVolume,
                                    soundEffectsVolume,
                                    1,
                                    0,
                                    1F
                                )

                            loadAdAndStartGame(gameDifficultyValue)
                        }
                    }
                }
            }
            DEFAULT_DIFFICULTY_EASY -> {
                setupUIForDifficultyPreviouslySelected()

                binding.textBodyOnGameDifficulty.apply {
                    visibility = View.VISIBLE
                    text = getString(
                        R.string.on_game_difficulty,
                        getString(R.string.game_difficulty_beginner)
                    )
                }

                binding.buttonStart.setOnClickListener {
                    if (soundEffectsEnabled)
                        soundPool?.play(
                            soundButtonClick,
                            soundEffectsVolume,
                            soundEffectsVolume,
                            0,
                            0,
                            1F
                        )

                    gameDifficultyValue = GAME_DIFFICULTY_VALUE_BEGINNER

                    loadAdAndStartGame(gameDifficultyValue)
                }
            }
            DEFAULT_DIFFICULTY_MEDIUM -> {
                setupUIForDifficultyPreviouslySelected()

                binding.textBodyOnGameDifficulty.apply {
                    visibility = View.VISIBLE
                    text = getString(
                        R.string.on_game_difficulty,
                        getString(R.string.game_difficulty_medium)
                    )
                }

                binding.buttonStart.setOnClickListener {
                    if (soundEffectsEnabled)
                        soundPool?.play(
                            soundButtonClick,
                            soundEffectsVolume,
                            soundEffectsVolume,
                            0,
                            0,
                            1F
                        )

                    gameDifficultyValue = GAME_DIFFICULTY_VALUE_BEGINNER

                    loadAdAndStartGame(gameDifficultyValue)
                }
            }
            DEFAULT_DIFFICULTY_HARD -> {
                setupUIForDifficultyPreviouslySelected()

                binding.textBodyOnGameDifficulty.apply {
                    visibility = View.VISIBLE
                    text = getString(
                        R.string.on_game_difficulty,
                        getString(R.string.game_difficulty_hard)
                    )
                }

                binding.buttonStart.setOnClickListener {
                    if (soundEffectsEnabled)
                        soundPool?.play(
                            soundButtonClick,
                            soundEffectsVolume,
                            soundEffectsVolume,
                            0,
                            0,
                            1F
                        )

                    gameDifficultyValue = GAME_DIFFICULTY_VALUE_HARD

                    loadAdAndStartGame(gameDifficultyValue)
                }
            }
        }
    }

    private fun loadSoundEffects() {
        soundPool?.let { soundPool ->
            soundButtonClick = soundPool.load(requireContext(), R.raw.jaoreir_button_simple_01, 1)
            soundClick = soundPool.load(requireContext(), R.raw.brandondelehoy_series_of_clicks, 2)
        }
    }

    override fun onDestroyView() {
        if (soundEffectsEnabled) {
            soundPool?.release()
            soundPool = null
        }
        _binding = null

        super.onDestroyView()
    }
}