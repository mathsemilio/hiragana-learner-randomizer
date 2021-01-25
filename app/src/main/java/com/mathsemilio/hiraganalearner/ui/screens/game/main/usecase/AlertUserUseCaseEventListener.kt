package com.mathsemilio.hiraganalearner.ui.screens.game.main.usecase

import com.mathsemilio.hiraganalearner.ui.screens.game.main.ControllerState

interface AlertUserUseCaseEventListener {
    fun onPauseGameTimer()
    fun onControllerStateChanged(newControllerState: ControllerState)
    fun onPlayButtonClickSoundEffect()
    fun onPlaySuccessSoundEffect()
    fun onPlayErrorSoundEffect()
}
