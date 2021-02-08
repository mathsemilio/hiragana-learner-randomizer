package com.mathsemilio.hiraganalearner.ui.screens.game.result

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.*
import com.mathsemilio.hiraganalearner.ui.screens.common.BaseObservableView

class GameResultScreenViewImpl(inflater: LayoutInflater, container: ViewGroup?) :
    BaseObservableView<GameResultScreenView.Listener>(), GameResultScreenView {

    private lateinit var textViewYouGotSymbolsCorrectly: TextView
    private lateinit var textViewGameDifficulty: TextView
    private lateinit var textViewGamePerfectScores: TextView
    private lateinit var buttonHome: FloatingActionButton
    private lateinit var buttonPlayAgain: FloatingActionButton
    private lateinit var buttonShareScore: FloatingActionButton
    private lateinit var adViewGameResultScreen: AdView

    private var gameDifficultyValue = 0

    init {
        setRootView(inflater.inflate(R.layout.game_result_screen, container, false))
        initializeViews()
        attachClickListeners()
    }

    override fun setupUI(score: Int, difficultyValue: Int, perfectScores: Int) {
        gameDifficultyValue = difficultyValue

        if (score == 0) buttonShareScore.visibility = View.GONE

        setupYouGotSymbolsCorrectlyTextView(score)
        setupGameDifficultyTextView(difficultyValue)
        setupPerfectScoresNumberTextView(perfectScores)
    }

    override fun loadBannerAd(adRequest: AdRequest) {
        adViewGameResultScreen.loadAd(adRequest)
    }

    private fun initializeViews() {
        textViewYouGotSymbolsCorrectly = findViewById(R.id.text_headline_you_got_correctly)
        textViewGameDifficulty = findViewById(R.id.text_headline_game_difficulty_score_screen)
        textViewGamePerfectScores = findViewById(R.id.text_headline_perfect_scores_number_score_screen)
        buttonHome = findViewById(R.id.fab_home)
        buttonPlayAgain = findViewById(R.id.fab_play_again)
        buttonShareScore = findViewById(R.id.fab_share)
        adViewGameResultScreen = findViewById(R.id.adview_game_result_screen)
    }

    private fun setupYouGotSymbolsCorrectlyTextView(finalScore: Int) {
        textViewYouGotSymbolsCorrectly.text = when (finalScore) {
            1 -> context.getString(R.string.you_got_one_symbol_correctly, finalScore)
            PERFECT_SCORE -> context.getString(R.string.you_got_all_symbols_correctly)
            else -> context.getString(R.string.you_got_symbol_correctly, finalScore)
        }
    }

    private fun setupGameDifficultyTextView(difficultyValue: Int) {
        textViewGameDifficulty.text = when (difficultyValue) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> context.getString(R.string.game_difficulty_beginner)
            GAME_DIFFICULTY_VALUE_MEDIUM -> context.getString(R.string.game_difficulty_medium)
            GAME_DIFFICULTY_VALUE_HARD -> context.getString(R.string.game_difficulty_hard)
            else -> throw IllegalArgumentException(ILLEGAL_GAME_DIFFICULTY_VALUE)
        }
    }

    private fun setupPerfectScoresNumberTextView(perfectScores: Int) {
        textViewGamePerfectScores.text = perfectScores.toString()
    }

    private fun attachClickListeners() {
        buttonHome.setOnClickListener { onHomeButtonClicked() }
        buttonPlayAgain.setOnClickListener { onPlayAgainButtonClicked(gameDifficultyValue) }
        buttonShareScore.setOnClickListener { onShareScoreButtonClicked() }
    }

    private fun onHomeButtonClicked() {
        getListeners().forEach { it.onHomeButtonClicked() }
    }

    private fun onPlayAgainButtonClicked(difficultyValue: Int) {
        getListeners().forEach { it.onPlayAgainClicked(difficultyValue) }
    }

    private fun onShareScoreButtonClicked() {
        getListeners().forEach { it.onShareScoreButtonClicked() }
    }
}