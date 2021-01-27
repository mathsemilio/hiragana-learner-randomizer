package com.mathsemilio.hiraganalearner.data.preferences.repository

import android.content.Context
import com.mathsemilio.hiraganalearner.data.preferences.source.PreferencesSource

class PreferencesRepository(context: Context) {

    private val mPreferencesSource = PreferencesSource(context)

    fun setTrainingNotificationSwitchState(state: Boolean) =
        mPreferencesSource.setNotificationSwitchState(state)

    fun setTrainingNotificationTimeConfigured(timeConfigured: Long) =
        mPreferencesSource.setNotificationTimeConfigured(timeConfigured)

    fun incrementPerfectScoresValue() = mPreferencesSource.incrementPerfectScoreValue()

    fun clearPerfectScoresValue() = mPreferencesSource.clearPerfectScoresValue()

    fun setAppThemeValue(themeValue: Int) = mPreferencesSource.setAppThemeValue(themeValue)

    fun getTrainingNotificationSwitchState() = mPreferencesSource.getNotificationSwitchState()

    fun getTrainingNotificationTimeConfigured() = mPreferencesSource.getNotificationTimeConfigured()

    fun getGameDefaultOption() = mPreferencesSource.getGameDefaultOption()

    fun getPerfectScoresValue() = mPreferencesSource.getPerfectScoresValue()

    fun getSoundEffectsVolume() = mPreferencesSource.getSoundEffectsVolume()

    fun getAppThemeValue() = mPreferencesSource.getAppThemeValue()
}