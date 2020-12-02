package com.mathsemilio.hiraganalearner.data.preferences.repository

import android.content.Context
import com.mathsemilio.hiraganalearner.data.preferences.source.PreferencesSource

class PreferencesRepository(context: Context) {

    private val preferencesSource = PreferencesSource(context)

    fun saveNotificationSwitchState(switchState: Boolean) =
        preferencesSource.saveNotificationSwitchState(switchState)

    fun saveNotificationTimeConfigured(timeConfigured: Long) =
        preferencesSource.saveNotificationTimeConfigured(timeConfigured)

    fun incrementPerfectScoresValue() =
        preferencesSource.incrementPerfectScoreValue()

    fun saveAppThemeValue(themeValue: Int) =
        preferencesSource.saveAppThemeValue(themeValue)

    fun getNotificationSwitchState(): Boolean =
        preferencesSource.getNotificationSwitchState()

    fun getNotificationTimeConfigured(): Long =
        preferencesSource.getNotificationTimeConfigured()

    fun getGameDefaultOption(): String =
        preferencesSource.getGameDefaultOption()

    fun getPerfectScoresValue(): Int =
        preferencesSource.getPerfectScoresValue()

    fun getAppThemeValue(): Int =
        preferencesSource.getAppThemeValue()

    fun getSoundEffectsVolume(): Float =
        preferencesSource.getSoundEffectsVolume()

    fun clearPerfectScoresValue() =
        preferencesSource.clearPerfectScoresValue()
}