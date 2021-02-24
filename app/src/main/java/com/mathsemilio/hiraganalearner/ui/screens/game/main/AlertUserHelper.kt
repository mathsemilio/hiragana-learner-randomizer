package com.mathsemilio.hiraganalearner.ui.screens.game.main

import com.mathsemilio.hiraganalearner.common.observable.BaseObservable
import com.mathsemilio.hiraganalearner.ui.common.helper.DialogHelper

class AlertUserHelper(private val dialogHelper: DialogHelper) : BaseObservable<AlertUserHelper.Listener>() {

    interface Listener {
        fun onPauseGameTimer()
        fun onScreenStateChanged(newScreenState: ScreenState)
        fun onPlayButtonClickSoundEffect()
        fun onPlaySuccessSoundEffect()
        fun onPlayErrorSoundEffect()
    }

    fun alertUserOnExitGame(
        onDialogNegativeButtonClicked: () -> Unit,
        onDialogPositiveButtonClicked: () -> Unit
    ) {
        onPauseGameTimer()
        onPlayButtonClickSoundEffect()
        onCurrentScreenStateChanged(ScreenState.DIALOG_BEING_SHOWN)
        dialogHelper.showExitGameDialog(
            { onDialogNegativeButtonClicked() },
            {
                onDialogPositiveButtonClicked()
                onCurrentScreenStateChanged(ScreenState.TIMER_RUNNING)
            }
        )
    }

    fun alertUserOnGamePaused(onDialogPositiveButtonClicked: () -> Unit) {
        onPauseGameTimer()
        onPlayButtonClickSoundEffect()
        onCurrentScreenStateChanged(ScreenState.DIALOG_BEING_SHOWN)
        dialogHelper.showGamePausedDialog {
            onDialogPositiveButtonClicked()
            onCurrentScreenStateChanged(ScreenState.TIMER_RUNNING)
        }
    }

    fun alertUserOnCorrectAnswer(onDialogPositiveButtonClicked: () -> Unit) {
        onPlaySuccessSoundEffect()
        onCurrentScreenStateChanged(ScreenState.DIALOG_BEING_SHOWN)
        dialogHelper.showCorrectAnswerDialog {
            onDialogPositiveButtonClicked()
            onCurrentScreenStateChanged(ScreenState.TIMER_RUNNING)
        }
    }

    fun alertUserOnWrongAnswer(
        correctRomanization: String,
        onDialogPositiveButtonClicked: () -> Unit
    ) {
        onPlayErrorSoundEffect()
        onCurrentScreenStateChanged(ScreenState.DIALOG_BEING_SHOWN)
        dialogHelper.showWrongAnswerDialog(correctRomanization) {
            onDialogPositiveButtonClicked()
            onCurrentScreenStateChanged(ScreenState.TIMER_RUNNING)
        }
    }

    fun alertUserOnTimeOver(
        correctRomanization: String,
        onDialogPositiveButtonClicked: () -> Unit
    ) {
        onPlayErrorSoundEffect()
        onCurrentScreenStateChanged(ScreenState.DIALOG_BEING_SHOWN)
        dialogHelper.showTimeOverDialog(correctRomanization) {
            onDialogPositiveButtonClicked()
            onCurrentScreenStateChanged(ScreenState.TIMER_RUNNING)
        }
    }

    private fun onPauseGameTimer() {
        listeners.forEach { it.onPauseGameTimer() }
    }

    private fun onCurrentScreenStateChanged(newState: ScreenState) {
        listeners.forEach { it.onScreenStateChanged(newState) }
    }

    private fun onPlayButtonClickSoundEffect() {
        listeners.forEach { it.onPlayButtonClickSoundEffect() }
    }

    private fun onPlaySuccessSoundEffect() {
        listeners.forEach { it.onPlaySuccessSoundEffect() }
    }

    private fun onPlayErrorSoundEffect() {
        listeners.forEach { it.onPlayErrorSoundEffect() }
    }
}