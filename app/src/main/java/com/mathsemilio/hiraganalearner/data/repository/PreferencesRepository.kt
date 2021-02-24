package com.mathsemilio.hiraganalearner.data.repository

import android.content.Context
import com.mathsemilio.hiraganalearner.storage.preferences.PreferencesStorage

class PreferencesRepository(context: Context) {

    private val preferencesStorage = PreferencesStorage(context)

    fun setTrainingNotificationSwitchState(state: Boolean) =
        preferencesStorage.setNotificationSwitchState(state)

    fun setTrainingNotificationTimeConfigured(timeConfigured: Long) =
        preferencesStorage.setNotificationTimeConfigured(timeConfigured)

    fun incrementPerfectScoresValue() = preferencesStorage.incrementPerfectScoreValue()

    fun clearPerfectScoresValue() = preferencesStorage.clearPerfectScoresValue()

    fun setAppThemeValue(themeValue: Int) = preferencesStorage.setAppThemeValue(themeValue)

    val trainingNotificationSwitchState get() = preferencesStorage.getNotificationSwitchState()

    val trainingNotificationTimeConfigured get() = preferencesStorage.getNotificationTimeConfigured()

    val gameDefaultOption get() = preferencesStorage.getGameDefaultOption()

    val perfectScoresValue get() = preferencesStorage.getPerfectScoresValue()

    val soundEffectsVolume get() = preferencesStorage.getSoundEffectsVolume()

    val appThemeValue get() = preferencesStorage.getAppThemeValue()
}