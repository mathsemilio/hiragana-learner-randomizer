package com.mathsemilio.hiraganalearner.ui.screens.game.main

interface IGameMainScreenView {
    interface Listener {
        fun playClickSoundEffect()
        fun onExitButtonClicked()
        fun onPauseButtonClicked()
        fun onCheckAnswerClicked(selectedRomanization: String)
    }

    fun onControllerViewCreated(difficultyValue: Int)
    fun setGameDifficultyTextBasedOnDifficultyValue(difficultyValue: Int)
    fun updateGameScoreTextView(newScore: Int)
    fun updateCurrentHiraganaSymbol(newSymbol: String)
    fun updateProgressBarGameTimerProgress(countDownTime: Int)
    fun updateRomanizationOptionsGroup(romanizationList: List<String>)
    fun updateProgressBarGameProgress(progress: Int)
    fun clearRomanizationOptions()
}