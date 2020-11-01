package com.mathsemilio.hiraganalearner.ui.fragment

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
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.databinding.GameScoreScreenBinding
import com.mathsemilio.hiraganalearner.others.*

/**
 * Fragment class for the game score screen
 */
class GameScoreScreen : Fragment() {

    /**
     * Enum to represent the intended user action after the Interstitial Advertisement is shown.
     */
    private enum class UserAction { GO_TO_MAIN_GAME_SCREEN, GO_TO_WELCOME_SCREEN }

    private var _binding: GameScoreScreenBinding? = null
    private val binding get() = _binding!!
    private var soundPool: SoundPool? = null
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private lateinit var interstitialAd: InterstitialAd
    private lateinit var userAction: UserAction
    private var soundEffectsVolume = 0F
    private var soundEffectButtonClick = 0
    var score = 0
    var difficultyValue = 0
    var perfectScores = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.game_score_screen, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeVariables()

        attachOnBackPressedCallback()

        setupInterstitialAd()

        loadAdBanner()
    }

    private fun initializeVariables() {
        binding.gameScoreScreen = this
        binding.lifecycleOwner = this

        score = GameScoreScreenArgs.fromBundle(requireArguments()).score

        difficultyValue = GameScoreScreenArgs.fromBundle(requireArguments()).difficultyValue

        perfectScores = SharedPreferencesPerfectScores(requireContext()).retrievePerfectScore()

        soundEffectsVolume = PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getInt(SOUND_EFFECTS_VOLUME_PREFERENCE_KEY, 0).toFloat().div(10F)

        soundPool = setupSoundPool(1)
        soundPool?.let {
            soundEffectButtonClick =
                it.load(requireContext(), R.raw.jaoreir_button_simple_01, PRIORITY_LOW)
        }
    }

    private fun attachOnBackPressedCallback() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                userAction = UserAction.GO_TO_MAIN_GAME_SCREEN
                showAdAndNavigate()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    fun navigateToGameWelcomeScreen() {
        soundPool?.playSFX(soundEffectButtonClick, soundEffectsVolume, PRIORITY_LOW)
        userAction = UserAction.GO_TO_WELCOME_SCREEN
        showAdAndNavigate()
    }

    fun playGameAgain() {
        soundPool?.playSFX(soundEffectButtonClick, soundEffectsVolume, PRIORITY_LOW)
        userAction = UserAction.GO_TO_MAIN_GAME_SCREEN
        showAdAndNavigate()
    }

    fun shareGameScore() {
        soundPool?.playSFX(soundEffectButtonClick, soundEffectsVolume, PRIORITY_LOW)

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                if (score == PERFECT_SCORE)
                    getString(R.string.final_perfect_score)
                else resources.getQuantityString(R.plurals.game_score_plurals, score, score)
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

    private fun setupInterstitialAd() {
        interstitialAd = InterstitialAd(requireContext()).apply {
            adUnitId = getString(R.string.interstitialAdUnitId)
            adListener = (object : AdListener() {
                override fun onAdClosed() {
                    handleNavigation()
                }
            })
            loadAd(AdRequest.Builder().build())
        }
    }

    private fun loadAdBanner() {
        binding.gameScoreScreenBannerAd.loadAd(AdRequest.Builder().build())
    }

    private fun showAdAndNavigate() {
        if (interstitialAd.isLoaded) interstitialAd.show() else handleNavigation()
    }

    private fun handleNavigation() {
        when (userAction) {
            UserAction.GO_TO_MAIN_GAME_SCREEN -> {
                findNavController().navigate(
                    GameScoreScreenDirections.actionGameScoreScreenToMainGameScreen(
                        difficultyValue
                    )
                )
            }
            UserAction.GO_TO_WELCOME_SCREEN -> {
                findNavController().navigate(R.id.action_gameScoreScreen_to_gameWelcomeScreen)
            }
        }
    }

    override fun onDestroyView() {
        soundPool?.release()
        soundPool = null
        _binding = null

        super.onDestroyView()
    }
}