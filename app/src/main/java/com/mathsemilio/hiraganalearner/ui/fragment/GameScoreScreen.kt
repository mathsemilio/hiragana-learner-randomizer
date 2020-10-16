package com.mathsemilio.hiraganalearner.ui.fragment

import android.content.Intent
import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
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
    private var soundEffectsEnabled = true
    private var soundEffectsVolume = 0f
    private var soundButtonClick = 0
    private var gameScore = 0
    private var gameDifficultyValue = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = GameScoreScreenBinding.inflate(inflater, container, false)

        initializeVariables()

        setupUI()

        loadAdBanner()

        setupInterstitialAd()

        attachFABListeners()

        return binding.root
    }

    private fun initializeVariables() {
        gameScore = retrieveGameScore()

        gameDifficultyValue = retrieveGameDifficultyValue()

        soundEffectsVolume = PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getInt(SOUND_EFFECTS_VOLUME_PREF_KEY, 0).toFloat().div(10f)

        if (soundEffectsVolume == 0f) {
            soundEffectsEnabled = false
        } else {
            soundPool = setupSoundPool(1)
            soundPool?.let { soundPool ->
                soundButtonClick =
                    soundPool.load(requireContext(), R.raw.jaoreir_button_simple_01, 1)
            }
        }

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_gameScoreScreen_to_gameWelcomeScreen)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    private fun setupUI() {
        if (gameScore == PERFECT_SCORE)
            binding.textHeadlineFinalScore.text = getString(R.string.perfect_score)

        binding.textHeadlineGameScore.text = gameScore.toString()

        changeGradeIconVisibilityBasedOnGameScore(gameScore)

        binding.textHeadlineGameDifficultyScoreScreen.text = getGameDifficultyString()

        binding.textHeadlinePerfectScoresNumberScoreScreen.text =
            SharedPreferencesPerfectScores(requireContext()).retrievePerfectScore()
                .toString()
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

    private fun showAdAndNavigate() {
        if (interstitialAd.isLoaded) interstitialAd.show() else handleNavigation()
    }

    private fun loadAdBanner() {
        binding.gameScoreScreenBannerAd.loadAd(AdRequest.Builder().build())
    }

    private fun attachFABListeners() {
        binding.fabHome.setOnClickListener {
            if (soundEffectsEnabled)
                soundPool?.play(soundButtonClick, soundEffectsVolume, soundEffectsVolume, 0, 0, 1F)

            userAction = UserAction.GO_TO_WELCOME_SCREEN
            showAdAndNavigate()
        }

        binding.fabPlayAgain.setOnClickListener {
            if (soundEffectsEnabled)
                soundPool?.play(soundButtonClick, soundEffectsVolume, soundEffectsVolume, 0, 0, 1F)

            userAction = UserAction.GO_TO_MAIN_GAME_SCREEN
            showAdAndNavigate()
        }

        if (gameScore == 0) {
            binding.fabShare.visibility = View.GONE
        } else {
            binding.fabShare.setOnClickListener {
                if (soundEffectsEnabled)
                    soundPool?.play(
                        soundButtonClick,
                        soundEffectsVolume,
                        soundEffectsVolume,
                        0,
                        0,
                        1F
                    )
                shareGameScore()
            }
        }
    }

    private fun changeGradeIconVisibilityBasedOnGameScore(gameScore: Int) {
        when {
            gameScore <= 12 -> {
                binding.imageViewGrade4.visibility = View.GONE
                binding.imageViewGrade3.visibility = View.GONE
                binding.imageViewGrade2.visibility = View.GONE
            }
            gameScore in 13..23 -> {
                binding.imageViewGrade4.visibility = View.GONE
                binding.imageViewGrade3.visibility = View.GONE
            }
            gameScore in 24..47 -> {
                binding.imageViewGrade2.visibility = View.GONE
            }
        }
    }

    private fun shareGameScore() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                if (gameScore == PERFECT_SCORE)
                    getString(R.string.final_perfect_score)
                else resources.getQuantityString(R.plurals.game_score_plurals, gameScore, gameScore)
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

    private fun handleNavigation() {
        when (userAction) {
            UserAction.GO_TO_MAIN_GAME_SCREEN -> {
                findNavController().navigate(
                    GameScoreScreenDirections.actionGameScoreScreenToMainGameScreen(
                        gameDifficultyValue
                    )
                )
            }
            UserAction.GO_TO_WELCOME_SCREEN -> {
                findNavController().navigate(R.id.action_gameScoreScreen_to_gameWelcomeScreen)
            }
        }
    }

    private fun retrieveGameScore(): Int {
        return GameScoreScreenArgs.fromBundle(requireArguments()).gameScore
    }

    private fun retrieveGameDifficultyValue(): Int {
        return GameScoreScreenArgs.fromBundle(requireArguments()).gameDifficulty
    }

    private fun getGameDifficultyString(): String {
        return when (gameDifficultyValue) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> getString(R.string.game_difficulty_beginner)
            GAME_DIFFICULTY_VALUE_MEDIUM -> getString(R.string.game_difficulty_medium)
            else -> getString(R.string.game_difficulty_hard)
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