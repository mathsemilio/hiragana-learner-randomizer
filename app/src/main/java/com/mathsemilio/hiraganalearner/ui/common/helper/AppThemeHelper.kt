package com.mathsemilio.hiraganalearner.ui.common.helper

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.mathsemilio.hiraganalearner.common.APP_THEME_DARK_THEME
import com.mathsemilio.hiraganalearner.common.APP_THEME_FOLLOW_SYSTEM
import com.mathsemilio.hiraganalearner.common.APP_THEME_LIGHT_THEME
import com.mathsemilio.hiraganalearner.data.repository.PreferencesRepository

class AppThemeHelper(context: Context) {

    private val preferencesRepository = PreferencesRepository(context)

    val appThemeValue get() = preferencesRepository.appThemeValue

    fun setLightAppTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        preferencesRepository.setAppThemeValue(APP_THEME_LIGHT_THEME)
    }

    fun setDarkAppTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        preferencesRepository.setAppThemeValue(APP_THEME_DARK_THEME)
    }

    fun setFollowSystemAppTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        preferencesRepository.setAppThemeValue(APP_THEME_FOLLOW_SYSTEM)
    }

    fun setAppThemeFromPreferenceValue() {
        when (appThemeValue) {
            APP_THEME_LIGHT_THEME ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            APP_THEME_DARK_THEME ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            APP_THEME_FOLLOW_SYSTEM ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}