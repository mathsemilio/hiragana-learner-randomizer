package com.mathsemilio.hiraganalearner.data.preferences.source

import android.content.Context
import android.os.Build
import androidx.preference.PreferenceManager
import com.mathsemilio.hiraganalearner.common.*

class PreferencesSource(context: Context) {

    private val mDefaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val mSharedPreferencesEditor = mDefaultSharedPreferences.edit()

    fun saveNotificationSwitchState(state: Boolean) {
        mSharedPreferencesEditor.apply {
            putBoolean(NOTIFICATION_SWITCH_STATE_KEY, state)
            apply()
        }
    }

    fun saveNotificationTimeConfigured(timeConfigured: Long) {
        mSharedPreferencesEditor.apply {
            putLong(NOTIFICATION_TIME_CONFIGURED_KEY, timeConfigured)
            apply()
        }
    }

    fun incrementPerfectScoreValue() {
        mSharedPreferencesEditor.apply {
            putInt(PERFECT_SCORES_KEY, getPerfectScoresValue().inc())
            apply()
        }
    }

    fun getNotificationSwitchState(): Boolean {
        return mDefaultSharedPreferences.getBoolean(NOTIFICATION_SWITCH_STATE_KEY, false)
    }

    fun getNotificationTimeConfigured(): Long {
        return mDefaultSharedPreferences.getLong(NOTIFICATION_TIME_CONFIGURED_KEY, 0L)
    }

    fun getGameDefaultOption(): String {
        return mDefaultSharedPreferences.getString(DEFAULT_GAME_DIFFICULTY_PREFERENCE_KEY, "0")!!
    }

    fun getPerfectScoresValue(): Int {
        return mDefaultSharedPreferences.getInt(PERFECT_SCORES_KEY, 0)
    }

    fun getSoundEffectsVolume(): Float {
        return mDefaultSharedPreferences.getInt(SOUND_EFFECTS_VOLUME_PREFERENCE_KEY, 0)
            .toFloat().div(10F)
    }

    fun saveAppThemeValue(themeValue: Int) {
        mSharedPreferencesEditor.apply {
            putInt(APP_THEME_KEY, themeValue)
            apply()
        }
    }

    fun getAppThemeValue(): Int {
        return mDefaultSharedPreferences.getInt(
            APP_THEME_KEY,
            when (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                true -> 0
                false -> 2
            }
        )
    }

    fun clearPerfectScoresValue() {
        mSharedPreferencesEditor.apply {
            putInt(PERFECT_SCORES_KEY, 0)
            apply()
        }
    }
}