package com.mathsemilio.hiraganalearner.ui.screens.game.main.usecase

import com.mathsemilio.hiraganalearner.ui.screens.game.main.ControllerState

interface AlertUserUserCaseEventListener {
    fun onPauseGameTimer()
    fun onControllerStateChanged(newState: ControllerState)
    fun onPlayButtonClickSoundEffect()
    fun onPlaySuccessSoundEffect()
    fun onPlayErrorSoundEffect()
}
