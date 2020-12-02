package com.mathsemilio.hiraganalearner.ui.screens.welcome

import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.chip.Chip
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.commom.*
import com.mathsemilio.hiraganalearner.data.preferences.repository.PreferencesRepository
import com.mathsemilio.hiraganalearner.databinding.GameWelcomeScreenBinding
import com.mathsemilio.hiraganalearner.ui.commom.util.playSFX
import com.mathsemilio.hiraganalearner.ui.commom.util.setupAndLoadInterstitialAd
import com.mathsemilio.hiraganalearner.ui.commom.util.setupSoundPool

class GameWelcomeScreen : Fragment() {

    private var _binding: GameWelcomeScreenBinding? = null
    private val binding get() = _binding!!

    private var interstitialAd: InterstitialAd? = null

    private lateinit var preferencesRepository: PreferencesRepository

    private var soundPool: SoundPool? = null

    private var soundEffectsVolume = 0F
    private var soundEffectButtonClick = 0
    private var soundEffectClick = 0

    var difficultyValue = 0
    var defaultDifficultyValue = "0"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.game_welcome_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            gameWelcomeScreen = this@GameWelcomeScreen
            lifecycleOwner = this@GameWelcomeScreen
        }

        interstitialAd =
            setupAndLoadInterstitialAd("ca-app-pub-3940256099942544/1033173712") { startGame() }

        preferencesRepository = PreferencesRepository(requireContext())

        soundPool = setupSoundPool(maxAudioStreams = 1)

        soundEffectsVolume = preferencesRepository.getSoundEffectsVolume()

        defaultDifficultyValue = when (preferencesRepository.getGameDefaultOption()) {
            "0" -> SHOW_DIFFICULTY_OPTIONS
            "1" -> DEFAULT_DIFFICULTY_EASY
            "2" -> DEFAULT_DIFFICULTY_MEDIUM
            "3" -> DEFAULT_DIFFICULTY_HARD
            else -> throw IllegalArgumentException(INVALID_DEFAULT_GAME_OPTION_EXCEPTION)
        }.also {
            when (it) {
                SHOW_DIFFICULTY_OPTIONS -> attachDifficultyChipGroupListener()
                DEFAULT_DIFFICULTY_EASY -> difficultyValue = GAME_DIFFICULTY_VALUE_BEGINNER
                DEFAULT_DIFFICULTY_MEDIUM -> difficultyValue = GAME_DIFFICULTY_VALUE_MEDIUM
                DEFAULT_DIFFICULTY_HARD -> difficultyValue = GAME_DIFFICULTY_VALUE_HARD
            }
        }

        loadSoundEffects()
    }

    private fun loadSoundEffects() {
        soundPool?.apply {
            soundEffectButtonClick =
                load(requireContext(), R.raw.jaoreir_button_simple_01, PRIORITY_LOW)
            soundEffectClick =
                load(requireContext(), R.raw.brandondelehoy_series_of_clicks, PRIORITY_MEDIUM)
        }
    }

    private fun attachDifficultyChipGroupListener() {
        binding.chipGroupGameDifficulty.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == -1) {
                binding.buttonStart.isEnabled = false
            } else {
                soundPool?.playSFX(soundEffectClick, soundEffectsVolume, PRIORITY_LOW)

                binding.buttonStart.isEnabled = true

                val checkedRadioButton = group.findViewById<Chip>(checkedId)

                difficultyValue = when (checkedRadioButton.text.toString()) {
                    getString(R.string.game_difficulty_beginner) -> GAME_DIFFICULTY_VALUE_BEGINNER
                    getString(R.string.game_difficulty_medium) -> GAME_DIFFICULTY_VALUE_MEDIUM
                    getString(R.string.game_difficulty_hard) -> GAME_DIFFICULTY_VALUE_HARD
                    else -> throw IllegalArgumentException(INVALID_GAME_DIFFICULTY_VALUE_EXCEPTION)
                }
            }
        }
    }

    fun navigateToSettingsFragment() {
        findNavController().navigate(R.id.action_gameWelcomeScreen_to_settingsFragment)
    }

    fun showAd() {
        soundPool?.playSFX(soundEffectButtonClick, soundEffectsVolume, PRIORITY_LOW)
        interstitialAd?.run { if (isLoaded) show() else startGame() }
    }

    private fun startGame() {
        findNavController()
            .navigate(
                GameWelcomeScreenDirections
                    .actionGameWelcomeScreenToGameMainScreen(difficultyValue)
            )
    }

    override fun onDestroyView() {
        soundPool?.release()
        soundPool = null
        _binding = null
        interstitialAd = null
        super.onDestroyView()
    }
}