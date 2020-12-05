package com.mathsemilio.hiraganalearner.ui.screens.score

import android.content.Intent
import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.commom.PERFECT_SCORE
import com.mathsemilio.hiraganalearner.commom.PRIORITY_LOW
import com.mathsemilio.hiraganalearner.data.preferences.repository.PreferencesRepository
import com.mathsemilio.hiraganalearner.databinding.GameScoreScreenBinding
import com.mathsemilio.hiraganalearner.ui.commom.util.playSFX
import com.mathsemilio.hiraganalearner.ui.commom.util.setupAndLoadInterstitialAd
import com.mathsemilio.hiraganalearner.ui.commom.util.setupSoundPool

class GameScoreScreen : Fragment() {

    private enum class UserAction { GO_TO_MAIN_GAME_SCREEN, GO_TO_WELCOME_SCREEN }

    private var _binding: GameScoreScreenBinding? = null
    private val binding get() = _binding!!

    private var interstitialAd: InterstitialAd? = null

    private lateinit var preferencesRepository: PreferencesRepository
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private lateinit var userAction: UserAction

    private var soundPool: SoundPool? = null

    private var soundEffectsVolume = 0F
    private var soundEffectButtonClick = 0

    var score = 0
    var difficultyValue = 0
    var perfectScores = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.game_score_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gameScoreScreen = this

        preferencesRepository = PreferencesRepository(requireContext())

        interstitialAd = setupAndLoadInterstitialAd(getString(R.string.interstitialAdUnitId)) {
            handleNavigation()
        }

        soundPool = setupSoundPool(maxAudioStreams = 1)

        GameScoreScreenArgs.fromBundle(requireArguments()).let { args ->
            score = args.score
            difficultyValue = args.difficultyValue
        }

        perfectScores = preferencesRepository.getPerfectScoresValue()

        setupOnBackPressedCallback()

        loadAdBanner()

        loadSoundEffects()
    }

    private fun setupOnBackPressedCallback() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                userAction = UserAction.GO_TO_MAIN_GAME_SCREEN
                showAd()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    private fun loadSoundEffects() {
        soundPool?.apply {
            soundEffectButtonClick =
                load(requireContext(), R.raw.jaoreir_button_simple_01, PRIORITY_LOW)
        }
    }

    fun navigateToWelcomeScreen() {
        soundPool?.playSFX(soundEffectButtonClick, soundEffectsVolume, PRIORITY_LOW)
        userAction = UserAction.GO_TO_WELCOME_SCREEN
        showAd()
    }

    fun playAgain() {
        soundPool?.playSFX(soundEffectButtonClick, soundEffectsVolume, PRIORITY_LOW)
        userAction = UserAction.GO_TO_MAIN_GAME_SCREEN
        showAd()
    }

    fun shareScore() {
        soundPool?.playSFX(soundEffectButtonClick, soundEffectsVolume, PRIORITY_LOW)

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                if (score == PERFECT_SCORE)
                    getString(R.string.share_perfect_final_score)
                else getString(R.string.share_final_score)
            )
            type = "text/plain"
        }

        startActivity(
            Intent.createChooser(
                sendIntent,
                getString(R.string.game_score_create_chooser_title)
            )
        )
    }

    private fun loadAdBanner() {
        binding.gameScoreScreenBannerAd.loadAd(AdRequest.Builder().build())
    }

    private fun showAd() {
        interstitialAd?.run { if (isLoaded) show() else handleNavigation() }
    }

    private fun handleNavigation() {
        when (userAction) {
            UserAction.GO_TO_MAIN_GAME_SCREEN ->
                findNavController().navigate(
                    GameScoreScreenDirections.actionGameScoreScreenToMainGameScreen(
                        difficultyValue
                    )
                )
            UserAction.GO_TO_WELCOME_SCREEN ->
                findNavController().navigate(R.id.action_gameScoreScreen_to_gameWelcomeScreen)
        }
    }

    override fun onDestroyView() {
        soundPool?.release()
        soundPool = null
        _binding = null
        interstitialAd = null
        super.onDestroyView()
    }
}