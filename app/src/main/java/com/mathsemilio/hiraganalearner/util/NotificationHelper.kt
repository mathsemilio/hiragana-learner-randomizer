package com.mathsemilio.hiraganalearner.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.ui.activity.MainActivity

class NotificationHelper(context: Context) {

    private val intentLaunchMainActivity = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    private val pendingIntent: PendingIntent =
        PendingIntent.getActivity(context, 0, intentLaunchMainActivity, 0)

    fun buildNotificationCompat(context: Context) {
        val notificationBuilder =
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
                setSmallIcon(R.drawable.ic_hiragana_learner_logo)
                setContentTitle(context.getString(R.string.training_notification_content_title))
                setContentText(context.getString(R.string.training_notification_content_text))
                priority = NotificationCompat.PRIORITY_DEFAULT
                setContentIntent(pendingIntent)
                setAutoCancel(true)
            }

        NotificationManagerCompat.from(context).apply {
            notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    fun createTrainingNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = CHANNEL_NAME
            val channelDescription = CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                channelName,
                importance
            ).apply {
                description = channelDescription
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}