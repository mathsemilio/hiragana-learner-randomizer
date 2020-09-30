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
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.databinding.GameScoreScreenBinding
import com.mathsemilio.hiraganalearner.others.*

/**
 * Fragment class for the game score screen
 */
class GameScoreScreen : Fragment() {

    private var _binding: GameScoreScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var soundPool: SoundPool
    private lateinit var onBackPressedCallback: OnBackPressedCallback
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
            soundButtonClick = soundPool.load(requireContext(), R.raw.jaoreir_button_simple_01, 1)
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

    /**
     * Sets up the UI for this fragment
     */
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

    /**
     * Attaches listeners for the Floating Action Buttons in this Fragment.
     */
    private fun attachFABListeners() {
        binding.fabHome.setOnClickListener {
            if (soundEffectsEnabled)
                soundPool.play(soundButtonClick, soundEffectsVolume, soundEffectsVolume, 0, 0, 1F)

            findNavController().navigate(R.id.action_gameScoreScreen_to_gameWelcomeScreen)
        }

        binding.fabPlayAgain.setOnClickListener {
            if (soundEffectsEnabled)
                soundPool.play(soundButtonClick, soundEffectsVolume, soundEffectsVolume, 0, 0, 1F)

            val action = GameScoreScreenDirections
                .actionGameScoreScreenToMainGameScreen(gameDifficultyValue)

            findNavController().navigate(action)
        }

        if (gameScore == 0) {
            binding.fabShare.visibility = View.GONE
        } else {
            binding.fabShare.setOnClickListener {
                if (soundEffectsEnabled)
                    soundPool.play(
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

    /**
     * Based on the game score value, it will change the visibility value of the grade ImageViews
     * to reflect the user success.
     *
     * @param gameScore The game score to be evaluated.
     */
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

    /**
     * Builds an intent chooser, enabling the user to share his game score.
     */
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

        val shareIntent =
            Intent.createChooser(sendIntent, getString(R.string.game_score_create_chooser_title))

        startActivity(shareIntent)
    }

    /**
     * Returns the game score value from the argument bundle.
     *
     * @return The game score from the arguments bundle.
     */
    private fun retrieveGameScore(): Int {
        return GameScoreScreenArgs.fromBundle(requireArguments()).gameScore
    }

    /**
     * Returns the game difficulty value from the argument bundle.
     *
     * @return The game difficulty from the arguments bundle.
     */
    private fun retrieveGameDifficultyValue(): Int {
        return GameScoreScreenArgs.fromBundle(requireArguments()).gameDifficulty
    }

    /**
     * Returns a string which represents the game difficulty based on the value
     * from the argument bundle.
     *
     * @return String corresponding the game difficulty.
     */
    private fun getGameDifficultyString(): String {
        return when (gameDifficultyValue) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> getString(R.string.game_difficulty_beginner)
            GAME_DIFFICULTY_VALUE_MEDIUM -> getString(R.string.game_difficulty_medium)
            else -> getString(R.string.game_difficulty_hard)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}