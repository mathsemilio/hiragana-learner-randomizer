package com.mathsemilio.hiraganalearner.others

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.ads.MobileAds

@Suppress("unused")
class HiraganaRandomizerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {}
        setupAppTheme(SharedPreferencesAppTheme(this).retrieveThemeValue())
    }

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