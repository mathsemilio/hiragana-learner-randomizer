package com.mathsemilio.hiraganalearner.ui.others

import android.content.Context
import android.widget.Toast
import com.mathsemilio.hiraganalearner.R

class MessagesHelper(private val mContext: Context) {

    fun showTrainingReminderSetSuccessfullyMessage() {
        Toast.makeText(
            mContext,
            mContext.getString(R.string.preference_notification_set_toast_message),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun showTrainingReminderSetFailedMessage() {
        Toast.makeText(
            mContext,
            mContext.getString(R.string.preference_notification_time_in_future_toast_message),
            Toast.LENGTH_LONG
        ).show()
    }
}