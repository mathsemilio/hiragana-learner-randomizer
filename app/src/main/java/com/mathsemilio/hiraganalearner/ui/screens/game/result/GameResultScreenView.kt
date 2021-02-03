package com.mathsemilio.hiraganalearner.ui.screens.game.result

import com.google.android.gms.ads.AdRequest

interface GameResultScreenView {
    interface Listener {
        fun onHomeButtonClicked()
        fun onPlayAgainClicked(difficultyValue: Int)
        fun onShareScoreButtonClicked()
    }

    fun setupUI(score: Int, difficultyValue: Int, perfectScores: Int)
    fun loadBannerAd(adRequest: AdRequest)
}