package com.mathsemilio.hiraganalearner

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mathsemilio.hiraganalearner.ui.activity.MainActivity
import com.mathsemilio.hiraganalearner.util.NOTIFICATION_CHANNEL_ID

class NotificationWorkManager(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    companion object {
        const val NOTIFICATION_ID = 1000
        const val PENDING_INTENT_REQ_ID = 1001
    }

    override fun doWork(): Result {

        buildTrainingNotification()

        return Result.success()
    }

    private fun buildTrainingNotification() {
        val intentLaunchMainActivity = Intent(applicationContext, MainActivity::class.java)
            .apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                applicationContext,
                PENDING_INTENT_REQ_ID,
                intentLaunchMainActivity,
                0
            )

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                applicationContext.getString(R.string.training_notifications_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description =
                    applicationContext
                        .getString(R.string.training_notifications_channel_description)
                enableVibration(true)
            }

            notificationManager.createNotificationChannel(channel)
        }

        val trainingNotification =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID).apply {
                setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                setContentTitle(
                    applicationContext
                        .getString(R.string.training_notification_content_title)
                )
                setContentText(
                    applicationContext
                        .getString(R.string.training_notification_content_text)
                )
                setCategory(NotificationCompat.CATEGORY_REMINDER)
                priority = NotificationCompat.PRIORITY_HIGH
                setContentIntent(pendingIntent)
                setAutoCancel(true)
            }
        notificationManager.notify(NOTIFICATION_ID, trainingNotification.build())
    }
}