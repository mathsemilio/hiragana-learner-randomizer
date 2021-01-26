package com.mathsemilio.hiraganalearner.ui.screens.game.result

import com.google.android.gms.ads.AdRequest

interface GameResultScreenView {
    interface Listener {
        fun onHomeButtonClicked()
        fun onPlayAgainClicked(difficultyValue: Int)
        fun onShareScoreButtonClicked()
    }

    fun onControllerViewCreated(difficultyValue: Int, score: Int, perfectScores: Int)
    fun loadGameResultScreenBannerAd(adRequest: AdRequest)
}