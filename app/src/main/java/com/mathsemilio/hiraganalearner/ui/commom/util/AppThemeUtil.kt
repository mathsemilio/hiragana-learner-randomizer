package com.mathsemilio.hiraganalearner.ui.commom.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.mathsemilio.hiraganalearner.commom.APP_THEME_DARK_THEME
import com.mathsemilio.hiraganalearner.commom.APP_THEME_FOLLOW_SYSTEM
import com.mathsemilio.hiraganalearner.commom.APP_THEME_LIGHT_THEME
import com.mathsemilio.hiraganalearner.data.preferences.repository.PreferencesRepository

class AppThemeUtil(context: Context) {

    private val preferencesRepository = PreferencesRepository(context)

    fun getAppThemeValue(): Int {
        return preferencesRepository.getAppThemeValue()
    }

    fun setLightAppTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        preferencesRepository.saveAppThemeValue(APP_THEME_LIGHT_THEME)
    }

    fun setDarkAppTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        preferencesRepository.saveAppThemeValue(APP_THEME_DARK_THEME)
    }

    fun setFollowSystemAppTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        preferencesRepository.saveAppThemeValue(APP_THEME_FOLLOW_SYSTEM)
    }

    fun setAppThemeFromPreferenceValue() {
        when (preferencesRepository.getAppThemeValue()) {
            APP_THEME_LIGHT_THEME ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            APP_THEME_DARK_THEME ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            APP_THEME_FOLLOW_SYSTEM ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}