package com.mathsemilio.hiraganalearner.ui.commom

import androidx.fragment.app.Fragment
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.ui.commom.util.showMaterialDialog

object AlertUserHelper {

    fun Fragment.onCorrectAnswer(onPositiveButtonClicked: () -> Unit) {
        showMaterialDialog(
            getString(R.string.alert_dialog_correct_answer_title),
            getString(R.string.alert_dialog_correct_answer_message),
            getString(R.string.alert_dialog_continue_button_text),
            negativeButtonText = null,
            isCancelable = false,
            positiveButtonListener = { _, _ -> onPositiveButtonClicked() },
            negativeButtonListener = null
        )
    }

    fun Fragment.onWrongAnswer(correctRomanization: String, onPositiveButtonClicked: () -> Unit) {
        showMaterialDialog(
            getString(R.string.alert_dialog_wrong_answer_title),
            getString(R.string.alert_dialog_wrong_answer_message, correctRomanization),
            getString(R.string.alert_dialog_continue_button_text),
            negativeButtonText = null,
            isCancelable = false,
            positiveButtonListener = { _, _ -> onPositiveButtonClicked() },
            negativeButtonListener = null
        )
    }

    fun Fragment.onTimeOver(correctRomanization: String, onPositiveButtonClicked: () -> Unit) {
        showMaterialDialog(
            getString(R.string.alert_dialog_time_over_title),
            getString(R.string.alert_dialog_time_over_message, correctRomanization),
            getString(R.string.alert_dialog_continue_button_text),
            negativeButtonText = null,
            isCancelable = false,
            positiveButtonListener = { _, _ -> onPositiveButtonClicked() },
            negativeButtonListener = null
        )
    }

    fun Fragment.onGameIsPaused(onPositiveButtonClicked: () -> Unit) {
        showMaterialDialog(
            getString(R.string.alert_dialog_game_paused_title),
            getString(R.string.alert_dialog_game_paused_message),
            getString(R.string.alert_dialog_game_paused_positive_button_text),
            negativeButtonText = null,
            isCancelable = false,
            positiveButtonListener = { _, _ -> onPositiveButtonClicked() },
            negativeButtonListener = null
        )
    }

    fun Fragment.onExitGame(
        onNegativeButtonClicked: () -> Unit,
        onPositiveButtonClicked: () -> Unit
    ) {
        showMaterialDialog(
            getString(R.string.alert_dialog_exit_game_title),
            getString(R.string.alert_dialog_exit_game_message),
            getString(R.string.alert_dialog_exit_game_positive_button_text),
            getString(R.string.alert_dialog_exit_button_text),
            isCancelable = false,
            positiveButtonListener = { _, _ -> onPositiveButtonClicked() },
            negativeButtonListener = { _, _ -> onNegativeButtonClicked() }
        )
    }

    fun Fragment.onClearPerfectScoresPreference(onPositiveButtonClicked: () -> Unit) {
        showMaterialDialog(
            getString(R.string.clear_perfect_score_dialog_title),
            getString(R.string.clear_perfect_score_dialog_message),
            getString(R.string.clear_perfect_score_dialog_positive_button_text),
            getString(R.string.clear_perfect_score_dialog_negative_button_text),
            isCancelable = true,
            positiveButtonListener = { _, _ -> onPositiveButtonClicked() },
            negativeButtonListener = null
        )
    }
}