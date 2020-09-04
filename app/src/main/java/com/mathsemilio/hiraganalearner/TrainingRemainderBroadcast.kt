package com.mathsemilio.hiraganalearner

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mathsemilio.hiraganalearner.util.NotificationHelper

class TrainingRemainderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        NotificationHelper(context!!).buildNotificationCompat(context)
    }
}