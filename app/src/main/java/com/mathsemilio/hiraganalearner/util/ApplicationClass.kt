package com.mathsemilio.hiraganalearner.util

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

private const val APP_THEME = "app_theme"

class ApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()

        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(applicationContext)

        AppCompatDelegate.setDefaultNightMode(
            getUIModeConstantBasedOnPrefValue(sharedPreferences.getString(APP_THEME, "0")!!)
        )
    }

    private fun getUIModeConstantBasedOnPrefValue(prefValue: String): Int {
        return when (prefValue) {
            "1" -> AppCompatDelegate.MODE_NIGHT_YES
            "2" -> AppCompatDelegate.MODE_NIGHT_NO
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    }
}