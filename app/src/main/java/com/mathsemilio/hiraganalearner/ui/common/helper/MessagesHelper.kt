package com.mathsemilio.hiraganalearner.ui.common.helper

import android.content.Context
import android.widget.Toast
import com.mathsemilio.hiraganalearner.R

class MessagesHelper(private val context: Context) {

    fun showTrainingReminderSetSuccessfullyMessage() {
        Toast.makeText(
            context,
            context.getString(R.string.preference_notification_set_toast_message),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun showTrainingReminderSetFailedMessage() {
        Toast.makeText(
            context,
            context.getString(R.string.preference_notification_time_in_future_toast_message),
            Toast.LENGTH_LONG
        ).show()
    }
}