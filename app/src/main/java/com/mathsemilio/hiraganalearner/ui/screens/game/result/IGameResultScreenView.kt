package com.mathsemilio.hiraganalearner.ui.screens.game.result

interface IGameResultScreenView {
    interface Listener {
        fun onHomeButtonClicked()
        fun onPlayAgainClicked(difficultyValue: Int)
        fun onShareScoreButtonClicked()
    }

    fun onControllerViewCreated(difficultyValue: Int, score: Int, perfectScores: Int)
}