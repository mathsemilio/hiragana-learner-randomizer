package com.mathsemilio.hiraganalearner.others

import androidx.fragment.app.Fragment
import com.mathsemilio.hiraganalearner.R

object AlertUser {

    fun Fragment.onCorrectAnswer(handleCorrectAnswerEvent: () -> Unit) {
        showMaterialDialog(
            getString(R.string.alert_dialog_correct_answer_title),
            getString(R.string.alert_dialog_correct_answer_message),
            getString(R.string.alert_dialog_continue_button_text),
            negativeButtonText = null,
            isCancelable = false,
            positiveButtonListener = { _, _ -> handleCorrectAnswerEvent() },
            negativeButtonListener = null
        )
    }

    fun Fragment.onWrongAnswer(handleWrongAnswerEvent: () -> Unit) {
        showMaterialDialog(
            getString(R.string.alert_dialog_wrong_answer_title),
            getString(R.string.alert_dialog_wrong_answer_message),
            getString(R.string.alert_dialog_continue_button_text),
            negativeButtonText = null,
            isCancelable = false,
            positiveButtonListener = { _, _ -> handleWrongAnswerEvent() },
            negativeButtonListener = null
        )
    }

    fun Fragment.onTimeOver(handleTimeOverEvent: () -> Unit) {
        showMaterialDialog(
            getString(R.string.alert_dialog_time_over_title),
            getString(R.string.alert_dialog_time_over_message),
            getString(R.string.alert_dialog_continue_button_text),
            negativeButtonText = null,
            isCancelable = false,
            positiveButtonListener = { _, _ -> handleTimeOverEvent() },
            negativeButtonListener = null
        )
    }

    fun Fragment.onGameIsPaused(handleOnGameResumeEvent: () -> Unit) {
        showMaterialDialog(
            getString(R.string.alert_dialog_game_paused_title),
            getString(R.string.alert_dialog_game_paused_message),
            getString(R.string.alert_dialog_game_paused_positive_button_text),
            negativeButtonText = null,
            isCancelable = false,
            positiveButtonListener = { _, _ -> handleOnGameResumeEvent() },
            negativeButtonListener = null
        )
    }

    fun Fragment.onExitGame(
        handleOnGameExitEvent: () -> Unit,
        handleOnExitGameCancelledEvent: () -> Unit
    ) {
        showMaterialDialog(
            getString(R.string.alert_dialog_exit_game_title),
            getString(R.string.alert_dialog_exit_game_message),
            getString(R.string.alert_dialog_exit_game_positive_button_text),
            getString(R.string.alert_dialog_exit_button_text),
            isCancelable = false,
            positiveButtonListener = { _, _ -> handleOnExitGameCancelledEvent() },
            negativeButtonListener = { _, _ -> handleOnGameExitEvent() }
        )
    }

    fun Fragment.onClearPerfectScoresPreference(handleClearPerfectScoresEvent: () -> Unit) {
        showMaterialDialog(
            getString(R.string.clear_perfect_score_dialog_title),
            getString(R.string.clear_perfect_score_dialog_message),
            getString(R.string.clear_perfect_score_dialog_positive_button_text),
            getString(R.string.clear_perfect_score_dialog_negative_button_text),
            isCancelable = true,
            positiveButtonListener = { _, _ -> handleClearPerfectScoresEvent() },
            negativeButtonListener = null
        )
    }
}