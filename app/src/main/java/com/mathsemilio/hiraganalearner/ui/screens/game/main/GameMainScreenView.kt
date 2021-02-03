package com.mathsemilio.hiraganalearner.ui.screens.game.main

interface GameMainScreenView {
    interface Listener {
        fun playClickSoundEffect()
        fun onExitButtonClicked()
        fun onPauseButtonClicked()
        fun onCheckAnswerClicked(selectedRomanization: String)
    }

    fun setupUI(difficultyValue: Int)
    fun setGameDifficultyTextBasedOnDifficultyValue(difficultyValue: Int)
    fun updateGameScoreTextView(newScore: Int)
    fun updateCurrentHiraganaSymbol(newSymbol: String)
    fun updateProgressBarGameTimerProgressValue(updatedCountdownTime: Int)
    fun updateRomanizationOptionsGroup(updatedRomanizationList: List<String>)
    fun updateProgressBarGameProgressValue(updatedProgress: Int)
    fun clearRomanizationOptions()
}