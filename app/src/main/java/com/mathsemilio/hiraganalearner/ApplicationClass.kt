package com.mathsemilio.hiraganalearner

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.mathsemilio.hiraganalearner.util.APP_THEME_DARK_MODE
import com.mathsemilio.hiraganalearner.util.APP_THEME_FOLLOW_SYSTEM
import com.mathsemilio.hiraganalearner.util.APP_THEME_LIGHT_THEME
import com.mathsemilio.hiraganalearner.util.SharedPreferencesAppTheme

class ApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()

        setupAppTheme(SharedPreferencesAppTheme(this).retrieveThemeValue())
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