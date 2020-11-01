package com.mathsemilio.hiraganalearner.ui.fragment

import android.content.SharedPreferences
import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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

    private var _binding: GameWelcomeScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var defaultSharedPreferences: SharedPreferences
    private lateinit var interstitialAd: InterstitialAd
    private var soundPool: SoundPool? = null
    private var soundEffectsVolume = 0F
    private var soundEffectButtonClick = 0
    private var soundEffectClick = 0
    var difficultyValue = 0
    var defaultDifficultyValue = "0"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.game_welcome_screen, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeVariables()

        setupInterstitialAd()
    }

    private fun initializeVariables() {
        binding.gameWelcomeScreen = this
        binding.lifecycleOwner = this

        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        soundEffectsVolume =
            defaultSharedPreferences.getInt(SOUND_EFFECTS_VOLUME_PREFERENCE_KEY, 0).toFloat()
                .div(10F)

        defaultDifficultyValue =
            when (defaultSharedPreferences.getString(DEFAULT_GAME_DIFFICULTY_PREFERENCE_KEY, "0")) {
                "0" -> SHOW_DIFFICULTY_OPTIONS
                "1" -> DEFAULT_DIFFICULTY_EASY
                "2" -> DEFAULT_DIFFICULTY_MEDIUM
                "3" -> DEFAULT_DIFFICULTY_HARD
                else -> throw IllegalArgumentException("Invalid default difficulty value")
            }.also {
                when (it) {
                    SHOW_DIFFICULTY_OPTIONS -> attachListenerForDifficultyChipGroup()
                    DEFAULT_DIFFICULTY_EASY -> difficultyValue = GAME_DIFFICULTY_VALUE_BEGINNER
                    DEFAULT_DIFFICULTY_MEDIUM -> difficultyValue = GAME_DIFFICULTY_VALUE_MEDIUM
                    DEFAULT_DIFFICULTY_HARD -> difficultyValue = GAME_DIFFICULTY_VALUE_HARD
                }
            }

        soundPool = setupSoundPool(1).also { soundPool ->
            soundEffectButtonClick =
                soundPool.load(requireContext(), R.raw.jaoreir_button_simple_01, PRIORITY_MEDIUM)
            soundEffectClick =
                soundPool.load(
                    requireContext(),
                    R.raw.brandondelehoy_series_of_clicks,
                    PRIORITY_LOW
                )
        }
    }

    private fun attachListenerForDifficultyChipGroup() {
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
                    else -> throw IllegalArgumentException("Invalid difficulty value")
                }
            }
        }
    }

    private fun setupInterstitialAd() {
        interstitialAd = InterstitialAd(requireContext()).apply {
            adUnitId = getString(R.string.interstitialAdUnitId)
            adListener = (object : AdListener() {
                override fun onAdClosed() {
                    startGame()
                }
            })
            loadAd(AdRequest.Builder().build())
        }
    }

    fun loadAdAndStartGame() {
        soundPool?.playSFX(soundEffectButtonClick, soundEffectsVolume, PRIORITY_MEDIUM)
        if (interstitialAd.isLoaded) interstitialAd.show() else startGame()
    }

    private fun startGame() {
        findNavController()
            .navigate(
                GameWelcomeScreenDirections
                    .actionGameWelcomeScreenToGameMainScreen(difficultyValue)
            )
    }

    fun navigateToSettingsFragment() {
        binding.chipGroupGameDifficulty.clearCheck()
        findNavController().navigate(R.id.action_gameWelcomeScreen_to_settingsFragment)
    }

    override fun onDestroyView() {
        soundPool?.release()
        soundPool = null
        _binding = null

        super.onDestroyView()
    }
}