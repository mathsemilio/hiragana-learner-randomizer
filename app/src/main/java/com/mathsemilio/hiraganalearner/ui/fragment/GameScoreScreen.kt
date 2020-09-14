package com.mathsemilio.hiraganalearner.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.databinding.GameScoreScreenBinding
import com.mathsemilio.hiraganalearner.util.GAME_DIFFICULTY_VALUE_BEGINNER
import com.mathsemilio.hiraganalearner.util.GAME_DIFFICULTY_VALUE_MEDIUM
import com.mathsemilio.hiraganalearner.util.PERFECT_SCORE
import com.mathsemilio.hiraganalearner.util.SharedPreferencesPerfectScores

/**
 * Fragment class for the game score screen
 */
class GameScoreScreen : Fragment() {

    private lateinit var binding: GameScoreScreenBinding
    private var gameScore: Int? = null
    private var gameDifficultyValue: Int? = null

    //==========================================================================================
    // onCreateView
    //==========================================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = GameScoreScreenBinding.inflate(inflater, container, false)

        gameScore = retrieveGameScore()

        gameDifficultyValue = retrieveGameDifficultyValue()

        setupUI()

        attachFABListeners()

        return binding.root
    }

    //==========================================================================================
    // setupUI function
    //==========================================================================================
    /**
     * Sets up the UI for this fragment
     */
    private fun setupUI() {
        if (gameScore == PERFECT_SCORE)
            binding.textHeadlineFinalScore.text = getString(R.string.perfect_score)

        changeGradeIconVisibilityBasedOnGameScore(gameScore!!)

        binding.textHeadlineGameScore.text = gameScore.toString()

        binding.textHeadlineGameDifficultyScoreScreen.text = getGameDifficultyString()

        binding.textHeadlinePerfectScoresNumberScoreScreen.text =
            SharedPreferencesPerfectScores(requireContext()).retrievePerfectScore()
                .toString()
    }

    //==========================================================================================
    // attachFABListeners function
    //==========================================================================================
    /**
     * Attaches listeners for the Floating Action Buttons in this Fragment.
     */
    private fun attachFABListeners() {
        binding.fabHome.setOnClickListener {
            findNavController().navigate(R.id.action_gameScoreScreen_to_gameWelcomeScreen)
        }

        binding.fabPlayAgain.setOnClickListener {
            val action = GameScoreScreenDirections
                .actionGameScoreScreenToMainGameScreen(gameDifficultyValue!!)

            findNavController().navigate(action)
        }

        if (gameScore == 0) {
            binding.fabShare.visibility = View.GONE
        } else {
            binding.fabShare.setOnClickListener { shareGameScore() }
        }
    }

    //==========================================================================================
    // changeGradeIconVisibilityBasedOnGameScore function
    //==========================================================================================
    /**
     * Based on the game score value, it will change the visibility value of the grade ImageViews
     * to reflect the user success.
     *
     * @param gameScore The final game score to be evaluated.
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

    //==========================================================================================
    // shareGameScore function
    //==========================================================================================
    /**
     * Builds an intent chooser, enabling the user to share his game score.
     */
    private fun shareGameScore() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                resources.getQuantityString(R.plurals.game_score_plurals, gameScore!!, gameScore)
            )
            type = "text/plain"
        }

        val shareIntent =
            Intent.createChooser(sendIntent, getString(R.string.game_score_create_chooser_title))

        startActivity(shareIntent)
    }

    //==========================================================================================
    // retrieveGameScore function
    //==========================================================================================
    /**
     * Returns the game score value from the argument bundle.
     *
     * @return The game score from the arguments bundle.
     */
    private fun retrieveGameScore(): Int {
        return GameScoreScreenArgs.fromBundle(requireArguments()).gameScore
    }

    //==========================================================================================
    // retrieveGameDifficultyValue function
    //==========================================================================================
    /**
     * Returns the game difficulty value from the argument bundle.
     *
     * @return The game difficulty from the arguments bundle.
     */
    private fun retrieveGameDifficultyValue(): Int {
        return GameScoreScreenArgs.fromBundle(requireArguments()).gameDifficulty
    }

    //==========================================================================================
    // getGameDifficultyString function
    //==========================================================================================
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
}