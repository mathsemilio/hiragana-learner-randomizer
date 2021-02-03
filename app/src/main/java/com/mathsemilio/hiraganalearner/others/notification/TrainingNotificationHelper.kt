package com.mathsemilio.hiraganalearner.others.notification

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
import com.mathsemilio.hiraganalearner.common.NOTIFICATION_CHANNEL_ID
import com.mathsemilio.hiraganalearner.common.NOTIFICATION_ID
import com.mathsemilio.hiraganalearner.common.PENDING_INTENT_REQUEST_ID
import com.mathsemilio.hiraganalearner.common.TRAINING_NOTIFICATION_WORK_TAG
import com.mathsemilio.hiraganalearner.ui.screens.MainActivity
import java.util.concurrent.TimeUnit

class TrainingNotificationHelper(private val context: Context) {

    private val launchMainActivityIntent =
        Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

    private val trainingNotificationPendingIntent =
        PendingIntent.getActivity(
            context,
            PENDING_INTENT_REQUEST_ID,
            launchMainActivityIntent,
            0
        )

    private fun createNotificationChannel(notificationManager: NotificationManager) {
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

    private fun buildNotification(): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_hiragana_learner_notification_icon)
            setContentTitle(
                this@TrainingNotificationHelper.context.getString(R.string.training_notification_content_title)
            )
            setContentText(
                this@TrainingNotificationHelper.context.getString(R.string.training_notification_content_text)
            )
            setCategory(NotificationCompat.CATEGORY_REMINDER)
            priority = NotificationCompat.PRIORITY_HIGH
            setContentIntent(trainingNotificationPendingIntent)
            setAutoCancel(true)
        }
    }

    fun scheduleNotification(initialDelay: Long) {
        val notifyUserWorkRequest = OneTimeWorkRequestBuilder<TrainingNotificationWorker>()
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .addTag(TRAINING_NOTIFICATION_WORK_TAG)
            .build()

        WorkManager.getInstance(context).apply { enqueue(notifyUserWorkRequest) }
    }

    fun cancelNotification() {
        WorkManager.getInstance(context).apply {
            cancelAllWorkByTag(TRAINING_NOTIFICATION_WORK_TAG)
        }
    }

    fun notifyUser() {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(notificationManager)

        notificationManager.notify(NOTIFICATION_ID, buildNotification().build())
    }
}