package com.mathsemilio.hiraganalearner

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.mathsemilio.hiraganalearner.util.*

class ApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()

        createTrainingNotificationChannel(this)
        setupAppTheme(SharedPreferencesAppTheme(this).retrieveThemeValue())
    }

    private fun createTrainingNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = context.getString(R.string.training_notifications_channel_name)
            val channelDescription =
                context.getString(R.string.training_notifications_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                channelName,
                importance
            ).apply {
                description = channelDescription
                enableVibration(true)
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setupAppTheme(prefValue: Int) {
        when (prefValue) {
            APP_THEME_FOLLOW_SYSTEM ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            APP_THEME_DARK_MODE ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            APP_THEME_LIGHT_THEME ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}