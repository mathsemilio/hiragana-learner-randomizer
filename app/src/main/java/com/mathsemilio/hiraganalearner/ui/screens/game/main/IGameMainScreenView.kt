package com.mathsemilio.hiraganalearner.ui.screens.game.main

interface IGameMainScreenView {
    interface Listener {
        fun onPlayHiraganaSymbolSoundEffect(
            isHiraganaSoundsOn: Boolean,
            selectedRomanization: String
        )
        fun onExitButtonClicked()
        fun onPauseButtonClicked()
        fun onCheckAnswerClicked(selectedRomanization: String)
    }

    fun onControllerViewCreated(difficultyValue: Int, isHiraganaSoundsOn: Boolean)
    fun setGameDifficultyTextBasedOnDifficultyValue(difficultyValue: Int)
    fun updateGameScoreTextView(newScore: Int)
    fun updateCurrentHiraganaSymbol(newSymbol: String)
    fun updateProgressBarGameTimerProgress(countDownTime: Int)
    fun updateRomanizationOptionsGroup(romanizationList: List<String>)
    fun updateProgressBarGameProgress(progress: Int)
    fun clearRomanizationOptions()
}