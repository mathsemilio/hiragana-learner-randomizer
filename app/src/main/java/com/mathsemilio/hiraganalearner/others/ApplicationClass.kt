package com.mathsemilio.hiraganalearner.others

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

@Suppress("unused")
class ApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()

        setupAppTheme(SharedPreferencesAppTheme(this).retrieveThemeValue())
    }

    /**
     * Sets up the application theme based on the theme value from the SharedPreferences
     *
     * @param prefValue Integer value to be evaluated for selecting the app's theme.
     */
    private fun setupAppTheme(prefValue: Int) {
        when (prefValue) {
            APP_THEME_LIGHT_THEME ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            APP_THEME_DARK_MODE ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            APP_THEME_FOLLOW_SYSTEM ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}