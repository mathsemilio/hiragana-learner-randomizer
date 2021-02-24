package com.mathsemilio.hiraganalearner.storage.preferences

import android.content.Context
import android.os.Build
import androidx.preference.PreferenceManager
import com.mathsemilio.hiraganalearner.common.*

class PreferencesStorage(context: Context) {

    private val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val sharedPreferencesEditor = defaultSharedPreferences.edit()

    fun setNotificationSwitchState(state: Boolean) {
        sharedPreferencesEditor.apply {
            putBoolean(NOTIFICATION_SWITCH_STATE_KEY, state)
            apply()
        }
    }

    fun setNotificationTimeConfigured(timeConfigured: Long) {
        sharedPreferencesEditor.apply {
            putLong(NOTIFICATION_TIME_CONFIGURED_KEY, timeConfigured)
            apply()
        }
    }

    fun incrementPerfectScoreValue() {
        sharedPreferencesEditor.apply {
            putInt(PERFECT_SCORES_KEY, getPerfectScoresValue().inc())
            apply()
        }
    }

    fun setAppThemeValue(themeValue: Int) {
        sharedPreferencesEditor.apply {
            putInt(APP_THEME_KEY, themeValue)
            apply()
        }
    }

    fun getNotificationSwitchState(): Boolean {
        return defaultSharedPreferences.getBoolean(NOTIFICATION_SWITCH_STATE_KEY, false)
    }

    fun getNotificationTimeConfigured(): Long {
        return defaultSharedPreferences.getLong(NOTIFICATION_TIME_CONFIGURED_KEY, 0L)
    }

    fun getGameDefaultOption(): String {
        return defaultSharedPreferences.getString(DEFAULT_GAME_DIFFICULTY_PREFERENCE_KEY, "0")!!
    }

    fun getPerfectScoresValue(): Int {
        return defaultSharedPreferences.getInt(PERFECT_SCORES_KEY, 0)
    }

    fun getSoundEffectsVolume(): Float {
        return defaultSharedPreferences.getInt(SOUND_EFFECTS_VOLUME_PREFERENCE_KEY, 5)
            .toFloat().div(10F)
    }

    fun getAppThemeValue(): Int {
        return defaultSharedPreferences.getInt(
            APP_THEME_KEY,
            when (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                true -> 0
                false -> 2
            }
        )
    }

    fun clearPerfectScoresValue() {
        sharedPreferencesEditor.apply {
            putInt(PERFECT_SCORES_KEY, 0)
            apply()
        }
    }
}