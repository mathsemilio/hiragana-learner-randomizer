package com.mathsemilio.hiraganalearner.data.repository

import android.content.Context
import androidx.preference.PreferenceManager
import com.mathsemilio.hiraganalearner.others.*

class PreferencesRepository(context: Context) {

    private val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val editor = defaultSharedPreferences.edit()

    fun saveNotificationSwitchState(state: Boolean) {
        editor.apply {
            putBoolean(SWITCH_STATE_KEY, state)
            apply()
        }
    }

    fun saveNotificationTimeConfigured(timeConfigured: Long) {
        editor.apply {
            putLong(TIME_CONFIGURED_KEY, timeConfigured)
            apply()
        }
    }

    fun incrementPerfectScoreValue() {
        editor.apply {
            putInt(PERFECT_SCORES_KEY, getPerfectScoresValue().inc())
            apply()
        }
    }

    fun saveAppThemeValue(themeValue: Int) {
        editor.apply {
            putInt(APP_THEME_KEY, themeValue)
            apply()
        }
    }

    fun getNotificationSwitchState(): Boolean {
        return defaultSharedPreferences.getBoolean(SWITCH_STATE_KEY, false)
    }

    fun getNotificationTimeConfigured(): Long {
        return defaultSharedPreferences.getLong(TIME_CONFIGURED_KEY, 0L)
    }

    fun getPerfectScoresValue(): Int {
        return defaultSharedPreferences.getInt(PERFECT_SCORES_KEY, 0)
    }

    fun getAppThemeValue(): Int {
        return defaultSharedPreferences.getInt(APP_THEME_KEY, getAppDefaultThemeValue())
    }

    fun clearPerfectScoresValue() {
        editor.apply {
            putInt(PERFECT_SCORES_KEY, 0)
            apply()
        }
    }
}