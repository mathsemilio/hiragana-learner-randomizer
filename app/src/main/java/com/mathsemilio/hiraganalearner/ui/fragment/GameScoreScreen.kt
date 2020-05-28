package com.mathsemilio.hiraganalearner.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.databinding.GameScoreScreenBinding

/**
 * Fragment class for the game score screen
 */
class GameScoreScreen : Fragment() {

    //==========================================================================================
    // Class-wide variable
    //==========================================================================================
    // LateInit variable for the binding class pertaining the layout for this Fragment
    private lateinit var binding: GameScoreScreenBinding

    // Private variable to store the game score coming from the main game screen via SafeArgs
    private var gameScore: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Using the inflate function from the Binding class to inflate the layout for this
        // fragment
        binding = GameScoreScreenBinding.inflate(inflater, container, false)

        // Assigning the game score value to the value returned from the retrieveGameScore
        // function
        gameScore = retrieveGameScore()

        // Setting the text of the textHeadlineScoreNumber TextView as the value from the game
        // score variable
        binding.textHeadlineScoreNumber.text = gameScore.toString()

        // OnClickListener for the FinishGame button, which calls the navigateToWelcomeScreen
        // function
        binding.buttonFinishGame.setOnClickListener { navigateToWelcomeScreen() }

        // OnClickListener for the Share button, which calls the shareGameScore function
        binding.textButtonShare.setOnClickListener { shareGameScore(gameScore.toString()) }

        // Returning the root of the inflated layout
        return binding.root
    }

    //==========================================================================================
    // retrieveGameScore function
    //==========================================================================================
    /**
     * Private function that is responsible for fetching the game score value from the bundle
     *
     * @return - Integer that represents the Game Score value
     */
    private fun retrieveGameScore(): Int {
        return GameScoreScreenArgs.fromBundle(requireArguments()).gameScore
    }

    //==========================================================================================
    // shareGameScore function
    //==========================================================================================
    /**
     * Private function that is responsible for building a Share intent, enabling the user to
     * share his game score.
     *
     * @param gameScore - String that represents the game score value, to be used in the share
     * intent
     */
    private fun shareGameScore(gameScore: String) {
        // Creating a Intent object, with the action type ACTION.SEND
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, gameScore)
            type = "text/plain"
        }

        // Creating a Chooser, and passing the sendIntent created above and a String that
        // represents the chooser's title
        val shareIntent =
            Intent.createChooser(sendIntent, getString(R.string.game_score_create_chooser_title))

        // Calling startActivity to build and show the Intent Chooser
        startActivity(shareIntent)
    }

    //==========================================================================================
    // navigateToWelcomeScreen function
    //==========================================================================================
    /**
     * Private function that finds the NavController from the activity and calls the navigate
     * function, that receives a action id, to navigate from the score screen to welcome screen.
     */
    private fun navigateToWelcomeScreen() {
        activity?.findNavController(R.id.nav_host_fragment)
            ?.navigate(R.id.action_gameScoreScreen_to_gameWelcomeScreen)
    }
}