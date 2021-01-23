package com.mathsemilio.hiraganalearner.data.preferences.repository

import android.content.Context
import com.mathsemilio.hiraganalearner.data.preferences.source.PreferencesSource

class PreferencesRepository(context: Context) {

    private val mPreferencesSource = PreferencesSource(context)

    fun setTrainingNotificationSwitchState(switchState: Boolean) =
        mPreferencesSource.saveNotificationSwitchState(switchState)

    fun saveTrainingNotificationTimeConfigured(timeConfigured: Long) =
        mPreferencesSource.saveNotificationTimeConfigured(timeConfigured)

    fun incrementPerfectScoresValue() = mPreferencesSource.incrementPerfectScoreValue()

    fun clearPerfectScoresValue() = mPreferencesSource.clearPerfectScoresValue()

    fun saveAppThemeValue(themeValue: Int) = mPreferencesSource.saveAppThemeValue(themeValue)

    fun getTrainingNotificationSwitchState() = mPreferencesSource.getNotificationSwitchState()

    fun getTrainingNotificationTimeConfigured() = mPreferencesSource.getNotificationTimeConfigured()

    fun getGameDefaultOption() = mPreferencesSource.getGameDefaultOption()

    fun getPerfectScoresValue() = mPreferencesSource.getPerfectScoresValue()

    fun getIsHiraganaSoundsOnValue() = mPreferencesSource.getIsHiraganaSoundsOnValue()

    fun getSoundEffectsVolume() = mPreferencesSource.getSoundEffectsVolume()

    fun getAppThemeValue() = mPreferencesSource.getAppThemeValue()
}