package com.mathsemilio.hiraganalearner.data.preferences.source

import android.content.Context
import android.os.Build
import androidx.preference.PreferenceManager
import com.mathsemilio.hiraganalearner.commom.*

class PreferencesSource(context: Context) {

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

    fun getGameDefaultOption(): String {
        return defaultSharedPreferences.getString(DEFAULT_GAME_DIFFICULTY_PREFERENCE_KEY, "0")!!
    }

    fun getPerfectScoresValue(): Int {
        return defaultSharedPreferences.getInt(PERFECT_SCORES_KEY, 0)
    }

    fun getSoundEffectsVolume(): Float {
        return defaultSharedPreferences.getInt(SOUND_EFFECTS_VOLUME_PREFERENCE_KEY, 0)
            .toFloat().div(10F)
    }

    fun getAppThemeValue(): Int {
        return when (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            true -> defaultSharedPreferences.getInt(APP_THEME_KEY, 0)
            false -> defaultSharedPreferences.getInt(APP_THEME_KEY, 2)
        }
    }

    fun clearPerfectScoresValue() {
        editor.putInt(PERFECT_SCORES_KEY, 0)
    }
}