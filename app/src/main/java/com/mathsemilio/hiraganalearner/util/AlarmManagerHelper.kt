package com.mathsemilio.hiraganalearner.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.mathsemilio.hiraganalearner.TrainingRemainderBroadcastReceiver
import java.util.*

class AlarmManagerHelper {

    fun setAlarmManager(context: Context, timeSet: Calendar) {
        val intentLaunchBroadcastReceiver =
            Intent(context, TrainingRemainderBroadcastReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(
                    context,
                    TRAINING_NOTIFICATION_BROADCAST_REQUEST_ID,
                    intent,
                    0
                )
            }

        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            timeSet.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            intentLaunchBroadcastReceiver
        )
    }

    fun cancelAlarmManager(context: Context) {
        val broadcastIntent =
            Intent(context, TrainingRemainderBroadcastReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(
                    context,
                    TRAINING_NOTIFICATION_BROADCAST_REQUEST_ID,
                    intent,
                    0
                )
            }

        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(broadcastIntent)
    }
}