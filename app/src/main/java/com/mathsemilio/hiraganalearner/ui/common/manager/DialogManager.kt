/*
Copyright 2020 Matheus Menezes

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.mathsemilio.hiraganalearner.ui.common.manager

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.*
import com.mathsemilio.hiraganalearner.ui.dialog.promptdialog.PromptDialog
import com.mathsemilio.hiraganalearner.ui.dialog.theme.ThemeDialog
import com.mathsemilio.hiraganalearner.ui.dialog.timepicker.TimePickerDialog

class DialogManager(private val fragmentManager: FragmentManager, private val context: Context) {

    val currentDialogTag get() = fragmentManager.getCurrentDialogTag()

    fun showCorrectAnswerDialog() {
        val promptDialog = PromptDialog.newInstance(
            context.getString(R.string.alert_dialog_correct_answer_title),
            context.getString(R.string.alert_dialog_correct_answer_message),
            context.getString(R.string.alert_dialog_continue_button_text),
            null
        )
        promptDialog.show(fragmentManager, TAG_CORRECT_ANSWER_DIALOG)
    }

    fun showWrongAnswerDialog(correctRomanization: String) {
        val promptDialog = PromptDialog.newInstance(
            context.getString(R.string.alert_dialog_wrong_answer_title),
            context.getString(R.string.alert_dialog_wrong_answer_message, correctRomanization),
            context.getString(R.string.alert_dialog_continue_button_text),
            null
        )
        promptDialog.show(fragmentManager, TAG_WRONG_ANSWER_DIALOG)
    }

    fun showTimeOverDialog(correctRomanization: String) {
        val promptDialog = PromptDialog.newInstance(
            context.getString(R.string.alert_dialog_time_over_title),
            context.getString(R.string.alert_dialog_time_over_message, correctRomanization),
            context.getString(R.string.alert_dialog_continue_button_text),
            null
        )
        promptDialog.show(fragmentManager, TAG_TIME_OVER_DIALOG)
    }

    fun showGamePausedDialog() {
        val promptDialog = PromptDialog.newInstance(
            context.getString(R.string.alert_dialog_game_paused_title),
            context.getString(R.string.alert_dialog_game_paused_message),
            context.getString(R.string.alert_dialog_game_paused_positive_button_text),
            null
        )
        promptDialog.show(fragmentManager, TAG_GAME_PAUSED_DIALOG)
    }

    fun showExitGameDialog() {
        val promptDialog = PromptDialog.newInstance(
            context.getString(R.string.alert_dialog_exit_game_title),
            context.getString(R.string.alert_dialog_exit_game_message),
            context.getString(R.string.alert_dialog_exit_game_positive_button_text),
            context.getString(R.string.alert_dialog_exit_button_text)
        )
        promptDialog.show(fragmentManager, TAG_EXIT_GAME_DIALOG)
    }

    fun showClearPerfectScoresDialog() {
        val promptDialog = PromptDialog.newInstance(
            context.getString(R.string.clear_perfect_score_dialog_title),
            context.getString(R.string.clear_perfect_score_dialog_message),
            context.getString(R.string.clear_perfect_score_dialog_positive_button_text),
            context.getString(R.string.alert_dialog_cancel_button_text),
            isCancelable = true
        )
        promptDialog.show(fragmentManager, null)
    }

    fun showAppThemeDialog() {
        val appThemeDialog = ThemeDialog()
        appThemeDialog.show(fragmentManager, null)
    }

    fun showTimePickerDialog(): TimePickerDialog {
        val timePickerDialog = TimePickerDialog()
        timePickerDialog.show(fragmentManager, null)
        return timePickerDialog
    }
}