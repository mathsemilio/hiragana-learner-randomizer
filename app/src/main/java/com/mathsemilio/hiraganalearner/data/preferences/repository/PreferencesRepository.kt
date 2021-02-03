package com.mathsemilio.hiraganalearner.data.preferences.repository

import android.content.Context
import com.mathsemilio.hiraganalearner.data.preferences.source.PreferencesSource

class PreferencesRepository(context: Context) {

    private val preferencesSource = PreferencesSource(context)

    fun setTrainingNotificationSwitchState(state: Boolean) =
        preferencesSource.setNotificationSwitchState(state)

    fun setTrainingNotificationTimeConfigured(timeConfigured: Long) =
        preferencesSource.setNotificationTimeConfigured(timeConfigured)

    fun incrementPerfectScoresValue() = preferencesSource.incrementPerfectScoreValue()

    fun clearPerfectScoresValue() = preferencesSource.clearPerfectScoresValue()

    fun setAppThemeValue(themeValue: Int) = preferencesSource.setAppThemeValue(themeValue)

    val trainingNotificationSwitchState get() = preferencesSource.getNotificationSwitchState()

    val trainingNotificationTimeConfigured get() = preferencesSource.getNotificationTimeConfigured()

    val gameDefaultOption get() = preferencesSource.getGameDefaultOption()

    val perfectScoresValue get() = preferencesSource.getPerfectScoresValue()

    val soundEffectsVolume get() = preferencesSource.getSoundEffectsVolume()

    val appThemeValue get() = preferencesSource.getAppThemeValue()
}