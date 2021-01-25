package com.mathsemilio.hiraganalearner.ui.screens.game.welcome

interface GameWelcomeScreenView {
    interface Listener {
        fun onPlayClickSoundEffect()
        fun onSettingsIconClicked()
        fun onStartButtonClicked(difficultyValue: Int)
    }

    fun onControllerViewCreated(difficultyValueFromPreference: String)
    fun getDifficultyValue(): Int
}