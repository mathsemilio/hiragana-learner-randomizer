package com.mathsemilio.hiraganalearner.ui.others

import android.app.TimePickerDialog
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.showMaterialDialog
import com.mathsemilio.hiraganalearner.ui.screens.dialog.AppThemeDialog
import java.util.*

class DialogHelper(private val context: Context, private val fragmentManager: FragmentManager) {

    fun showCorrectAnswerDialog(onPositiveButtonClicked: () -> Unit) {
        context.apply {
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
    }

    fun showWrongAnswerDialog(correctRomanization: String, onPositiveButtonClicked: () -> Unit) {
        context.apply {
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
    }

    fun showTimeOverDialog(correctRomanization: String, onPositiveButtonClicked: () -> Unit) {
        context.apply {
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
    }

    fun showGamePausedDialog(onPositiveButtonClicked: () -> Unit) {
        context.apply {
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
    }

    fun showExitGameDialog(
        onNegativeButtonClicked: () -> Unit,
        onPositiveButtonClicked: () -> Unit
    ) {
        context.apply {
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
    }

    fun showClearPerfectScoresDialog(onPositiveButtonClicked: () -> Unit) {
        context.apply {
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

    fun showTimePickerDialog(
        calendar: Calendar,
        onTimeSetByUser: (hourSet: Int, minuteSet: Int) -> Unit,
        handleOnDialogDismiss: () -> Unit
    ): TimePickerDialog {
        return TimePickerDialog(
            context,
            { _, hourOfDay, minute -> onTimeSetByUser(hourOfDay, minute) },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            android.text.format.DateFormat.is24HourFormat(context)
        ).apply {
            setOnCancelListener { handleOnDialogDismiss() }
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    fun showAppThemeDialog() {
        val appThemeDialogFragment = AppThemeDialog()
        appThemeDialogFragment.show(fragmentManager, null)
    }
}