package com.mathsemilio.hiraganalearner.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.ConfigurationCompat
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

    //==========================================================================================
    // onCreateView
    //==========================================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflating the layout with the inflate function from the fragment's binding class
        val binding: GameScoreScreenBinding =
            GameScoreScreenBinding.inflate(inflater, container, false)

        val gameScore = retrieveGameScore()

        /*
        Checking if the game score is equal to 48 (a perfect score), if it is, a different
        string for the textBodyYouScored TextView will be shown
        */
        if (gameScore == PERFECT_SCORE) {
            binding.textHeadlineCongratulations.text =
                getString(R.string.perfect_score)
        }

        binding.textHeadlineGameScore.text = gameScore.toString()

        binding.textHeadlinePerfectScores.text =
            SharedPreferencesPerfectScores(requireContext()).retrievePerfectScoresNumber()
                .toString()

        binding.textHeadlineGameDifficulty.text = getGameDifficultyString()

        binding.fabHome.setOnClickListener {
            findNavController().navigate(R.id.action_gameScoreScreen_to_gameWelcomeScreen)
        }

        binding.fabPlayAgain.setOnClickListener {
            findNavController().navigate(R.id.action_gameScoreScreen_to_mainGameScreen)
        }

        /*
        Checking if the game score equals 0, if it is, the Share button will be hidden, else,
        a listener will be attached.
        */
        if (gameScore == 0) {
            binding.fabShare.visibility = View.GONE
        } else {
            binding.fabShare.setOnClickListener { shareGameScore(gameScore) }
        }

        // Returning the root of the inflated layout
        return binding.root
    }

    //==========================================================================================
    // retrieveGameScore function
    //==========================================================================================
    /**
     * Function that returns the game score value from the argument bundle.
     */
    private fun retrieveGameScore(): Int {
        return GameScoreScreenArgs.fromBundle(requireArguments()).gameScore
    }

    //==========================================================================================
    // getGameDifficultyString function
    //==========================================================================================
    /**
     * Function that returns a string which represents the game difficulty based on the value
     * from the argument bundle.
     */
    private fun getGameDifficultyString(): String {
        return when (GameScoreScreenArgs.fromBundle(requireArguments()).gameDifficulty) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> getString(R.string.game_difficulty_beginner)
            GAME_DIFFICULTY_VALUE_MEDIUM -> getString(R.string.game_difficulty_medium)
            else -> getString(R.string.game_difficulty_hard)
        }
    }

    //==========================================================================================
    // getGameScorePluralsBasedOnTheLocale function
    //==========================================================================================
    /**
     * Function that based on the device's locale, returns a game score quantity string.
     */
    private fun getGameScorePluralsBasedOnTheLocale(
        countryString: String,
        gameScoreForComparison: Int,
        gameScore: Int
    ): String {
        return when (countryString) {
            "BR" -> {
                resources.getQuantityString(
                    R.plurals.game_score_plurals_pt_br,
                    gameScoreForComparison,
                    gameScore
                )
            }
            else -> {
                resources.getQuantityString(
                    R.plurals.game_score_plurals_en,
                    gameScoreForComparison,
                    gameScore
                )
            }
        }
    }

    //==========================================================================================
    // shareGameScore function
    //==========================================================================================
    /**
     * Function that builds an intent chooser, to enabled a user to share his game score.
     */
    private fun shareGameScore(gameScore: Int) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                getGameScorePluralsBasedOnTheLocale(
                    ConfigurationCompat.getLocales(resources.configuration)[0].country,
                    gameScore,
                    gameScore
                )
            )
            type = "text/plain"
        }

        val shareIntent =
            Intent.createChooser(sendIntent, getString(R.string.game_score_create_chooser_title))

        startActivity(shareIntent)
    }
}