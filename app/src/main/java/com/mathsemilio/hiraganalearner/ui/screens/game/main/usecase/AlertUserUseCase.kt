package com.mathsemilio.hiraganalearner.ui.screens.game.main.usecase

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.mathsemilio.hiraganalearner.common.BaseObservable
import com.mathsemilio.hiraganalearner.ui.others.DialogHelper
import com.mathsemilio.hiraganalearner.ui.screens.game.main.ControllerState

class AlertUserUseCase(context: Context, fragmentManager: FragmentManager) :
    BaseObservable<AlertUserUseCaseEventListener>() {

    private val mDialogHelper = DialogHelper(context, fragmentManager)

    fun alertUserOnExitGame(
        onDialogNegativeButtonClicked: () -> Unit,
        onDialogPositiveButtonClicked: () -> Unit
    ) {
        pauseGameTimer()
        playButtonClickSoundEffect()
        changeCurrentControllerState(ControllerState.DIALOG_BEING_SHOWN)
        mDialogHelper.showExitGameDialog(
            { onDialogNegativeButtonClicked() },
            {
                onDialogPositiveButtonClicked()
                changeCurrentControllerState(ControllerState.RUNNING)
            }
        )
    }

    fun alertUserOnGamePaused(onDialogPositiveButtonClicked: () -> Unit) {
        pauseGameTimer()
        playButtonClickSoundEffect()
        changeCurrentControllerState(ControllerState.DIALOG_BEING_SHOWN)
        mDialogHelper.showGamePausedDialog {
            onDialogPositiveButtonClicked()
            changeCurrentControllerState(ControllerState.RUNNING)
        }
    }

    fun alertUserOnCorrectAnswer(onDialogPositiveButtonClicked: () -> Unit) {
        playSuccessSoundEffect()
        changeCurrentControllerState(ControllerState.DIALOG_BEING_SHOWN)
        mDialogHelper.showCorrectAnswerDialog {
            onDialogPositiveButtonClicked()
            changeCurrentControllerState(ControllerState.RUNNING)
        }
    }

    fun alertUserOnWrongAnswer(
        correctRomanization: String,
        onDialogPositiveButtonClicked: () -> Unit
    ) {
        playErrorSoundEffect()
        changeCurrentControllerState(ControllerState.DIALOG_BEING_SHOWN)
        mDialogHelper.showWrongAnswerDialog(correctRomanization) {
            onDialogPositiveButtonClicked()
            changeCurrentControllerState(ControllerState.RUNNING)
        }
    }

    fun alertUserOnTimeOver(
        correctRomanization: String,
        onDialogPositiveButtonClicked: () -> Unit
    ) {
        playErrorSoundEffect()
        changeCurrentControllerState(ControllerState.DIALOG_BEING_SHOWN)
        mDialogHelper.showTimeOverDialog(correctRomanization) {
            onDialogPositiveButtonClicked()
            changeCurrentControllerState(ControllerState.RUNNING)
        }
    }

    private fun pauseGameTimer() {
        getListeners().forEach { it.onPauseGameTimer() }
    }

    private fun changeCurrentControllerState(state: ControllerState) {
        getListeners().forEach { it.onControllerStateChanged(state) }
    }

    private fun playButtonClickSoundEffect() {
        getListeners().forEach { it.onPlayButtonClickSoundEffect() }
    }

    private fun playSuccessSoundEffect() {
        getListeners().forEach { it.onPlaySuccessSoundEffect() }
    }

    private fun playErrorSoundEffect() {
        getListeners().forEach { it.onPlayErrorSoundEffect() }
    }
}