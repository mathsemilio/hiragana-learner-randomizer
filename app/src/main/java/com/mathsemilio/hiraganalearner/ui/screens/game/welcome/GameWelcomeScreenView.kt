package com.mathsemilio.hiraganalearner.ui.screens.game.welcome

interface GameWelcomeScreenView {
    interface Listener {
        fun onPlayClickSoundEffect()
        fun onSettingsIconClicked()
        fun onStartButtonClicked(difficultyValue: Int)
    }

    fun setupUI(defaultDifficultyValueFromPreference: String)
    fun getDifficultyValue(): Int
}