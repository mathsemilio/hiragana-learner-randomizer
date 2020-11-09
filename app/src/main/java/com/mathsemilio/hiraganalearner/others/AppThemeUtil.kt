package com.mathsemilio.hiraganalearner.others

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.mathsemilio.hiraganalearner.data.repository.PreferencesRepository

class AppThemeUtil(context: Context) {

    private val preferencesRepository = PreferencesRepository(context)

    fun getAppThemeValue(): Int {
        return preferencesRepository.getAppThemeValue()
    }

    fun setAppTheme(themeValue: Int) {
        when (themeValue) {
            APP_THEME_LIGHT_THEME -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                preferencesRepository.saveAppThemeValue(APP_THEME_LIGHT_THEME)
            }
            APP_THEME_DARK_MODE -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                preferencesRepository.saveAppThemeValue(APP_THEME_DARK_MODE)
            }
            APP_THEME_FOLLOW_SYSTEM -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                preferencesRepository.saveAppThemeValue(APP_THEME_FOLLOW_SYSTEM)
            }
        }
    }

    fun setAppThemeFromPreferenceValue() {
        when (preferencesRepository.getAppThemeValue()) {
            APP_THEME_LIGHT_THEME ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            APP_THEME_DARK_MODE ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            APP_THEME_FOLLOW_SYSTEM ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}