package com.mathsemilio.hiraganalearner.ui.screens.game.result

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.*
import com.mathsemilio.hiraganalearner.ui.screens.common.BaseObservableView

class GameResultScreenViewImpl(inflater: LayoutInflater, container: ViewGroup?) :
    BaseObservableView<GameResultScreenView.Listener>(), GameResultScreenView {

    private lateinit var mTextViewYouGotSymbolsCorrectly: TextView
    private lateinit var mTextViewGameDifficulty: TextView
    private lateinit var mTextViewGamePerfectScores: TextView
    private lateinit var mButtonHome: FloatingActionButton
    private lateinit var mButtonPlayAgain: FloatingActionButton
    private lateinit var mButtonShareScore: FloatingActionButton

    private var mFinalScore = 0
    private var mGameDifficultyValue = 0

    init {
        setRootView(inflater.inflate(R.layout.game_result_screen, container, false))
        initializeViews()
    }

    override fun onControllerViewCreated(difficultyValue: Int, score: Int, perfectScores: Int) {
        mFinalScore = score
        mGameDifficultyValue = difficultyValue

        if (mFinalScore == 0) mButtonShareScore.visibility = View.GONE

        setupYouGotSymbolsCorrectlyTextView()
        setupGameDifficultyTextView()
        setupPerfectScoresNumberTextView(perfectScores)
        attachClickListeners()
    }

    private fun initializeViews() {
        mTextViewYouGotSymbolsCorrectly = findViewById(R.id.text_headline_you_got_correctly)
        mTextViewGameDifficulty = findViewById(R.id.text_headline_game_difficulty_score_screen)
        mTextViewGamePerfectScores =
            findViewById(R.id.text_headline_perfect_scores_number_score_screen)
        mButtonHome = findViewById(R.id.fab_home)
        mButtonPlayAgain = findViewById(R.id.fab_play_again)
        mButtonShareScore = findViewById(R.id.fab_share)
    }

    private fun setupYouGotSymbolsCorrectlyTextView() {
        mTextViewYouGotSymbolsCorrectly.text = when (mFinalScore) {
            1 -> getContext().getString(R.string.you_got_one_symbol_correctly, mFinalScore)
            PERFECT_SCORE -> getContext().getString(R.string.you_got_all_symbols_correctly)
            else -> getContext().getString(R.string.you_got_symbol_correctly, mFinalScore)
        }
    }

    private fun setupGameDifficultyTextView() {
        mTextViewGameDifficulty.text = when (mGameDifficultyValue) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> getContext().getString(R.string.game_difficulty_beginner)
            GAME_DIFFICULTY_VALUE_MEDIUM -> getContext().getString(R.string.game_difficulty_medium)
            GAME_DIFFICULTY_VALUE_HARD -> getContext().getString(R.string.game_difficulty_hard)
            else -> throw IllegalArgumentException(ILLEGAL_GAME_DIFFICULTY_VALUE)
        }
    }

    private fun setupPerfectScoresNumberTextView(perfectScores: Int) {
        mTextViewGamePerfectScores.text = perfectScores.toString()
    }

    private fun attachClickListeners() {
        mButtonHome.setOnClickListener { homeButtonClicked() }
        mButtonPlayAgain.setOnClickListener { playAgainButtonClicked(mGameDifficultyValue) }
        mButtonShareScore.setOnClickListener { shareScoreButtonClicked() }
    }

    private fun homeButtonClicked() {
        getListeners().forEach { it.onHomeButtonClicked() }
    }

    private fun playAgainButtonClicked(difficultyValue: Int) {
        getListeners().forEach { it.onPlayAgainClicked(difficultyValue) }
    }

    private fun shareScoreButtonClicked() {
        getListeners().forEach { it.onShareScoreButtonClicked() }
    }
}