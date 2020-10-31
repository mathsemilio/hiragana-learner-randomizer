package com.mathsemilio.hiraganalearner

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.ads.MobileAds
import com.mathsemilio.hiraganalearner.others.APP_THEME_DARK_MODE
import com.mathsemilio.hiraganalearner.others.APP_THEME_FOLLOW_SYSTEM
import com.mathsemilio.hiraganalearner.others.APP_THEME_LIGHT_THEME
import com.mathsemilio.hiraganalearner.others.SharedPreferencesAppTheme

@Suppress("unused")
class HiraganaRandomizerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {}
        setupAppTheme(SharedPreferencesAppTheme(this).retrieveThemeValue())
    }

    private fun setupAppTheme(preferenceValue: Int) {
        when (preferenceValue) {
            APP_THEME_LIGHT_THEME ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            APP_THEME_DARK_MODE ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            APP_THEME_FOLLOW_SYSTEM ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}