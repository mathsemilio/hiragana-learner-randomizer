package com.mathsemilio.hiraganalearner

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mathsemilio.hiraganalearner.ui.activity.MainActivity
import com.mathsemilio.hiraganalearner.util.NOTIFICATION_CHANNEL_ID
import com.mathsemilio.hiraganalearner.util.NOTIFICATION_ID

class TrainingRemainderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) createNotificationCompat(context)
    }

    private fun createNotificationCompat(context: Context) {
        val intentLaunchMainActivity = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intentLaunchMainActivity, 0)

        val notificationBuilder =
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
                setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                setContentTitle(context.getString(R.string.training_notification_content_title))
                setContentText(context.getString(R.string.training_notification_content_text))
                setCategory(NotificationCompat.CATEGORY_REMINDER)
                priority = NotificationCompat.PRIORITY_HIGH
                setContentIntent(pendingIntent)
                setAutoCancel(true)
            }

        NotificationManagerCompat.from(context).apply {
            notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }
}