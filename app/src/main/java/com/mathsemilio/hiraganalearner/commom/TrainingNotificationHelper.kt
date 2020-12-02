package com.mathsemilio.hiraganalearner.commom

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.others.NotificationWorker
import com.mathsemilio.hiraganalearner.ui.MainActivity
import java.util.concurrent.TimeUnit

object TrainingNotificationHelper {

    private fun createNotificationChannel(
        context: Context,
        notificationManager: NotificationManager
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                context.getString(R.string.training_notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description =
                    context.getString(R.string.training_notification_channel_description)
                enableVibration(true)
            }

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(context: Context): NotificationCompat.Builder {
        val launchMainActivityIntent = Intent(context, MainActivity::class.java)
            .apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            PENDING_INTENT_REQUEST_ID,
            launchMainActivityIntent,
            0
        )

        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_hiragana_learner_notification_icon)
            setContentTitle(
                context.getString(R.string.training_notification_content_title)
            )
            setContentText(
                context.getString(R.string.training_notification_content_text)
            )
            setCategory(NotificationCompat.CATEGORY_REMINDER)
            priority = NotificationCompat.PRIORITY_HIGH
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }
    }

    fun scheduleNotification(context: Context, initialDelay: Long) {
        val notifyUserWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .addTag(TRAINING_NOTIFICATION_WORK_TAG)
            .build()

        WorkManager.getInstance(context).apply { enqueue(notifyUserWorkRequest) }
    }

    fun cancelNotification(context: Context) {
        WorkManager.getInstance(context)
            .apply { cancelAllWorkByTag(TRAINING_NOTIFICATION_WORK_TAG) }
    }

    fun notifyUser(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(context, notificationManager)

        notificationManager.notify(NOTIFICATION_ID, buildNotification(context).build())
    }
}