/*
Copyright 2020 Matheus Menezes

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.mathsemilio.hiraganalearner.data.manager

import android.content.Context
import com.mathsemilio.hiraganalearner.storage.endpoint.PreferencesEndpoint

class PreferencesManager(context: Context) {

    private val preferencesEndpoint = PreferencesEndpoint(context)

    val defaultDifficultyValue get() = preferencesEndpoint.defaultDifficultyOption

    val soundEffectsVolume get() = preferencesEndpoint.soundEffectsVolume

    val trainingNotificationSwitchState get() = preferencesEndpoint.trainingNotificationSwitchState

    val trainingNotificationTimeSet get() = preferencesEndpoint.trainingNotificationTimeSet

    val perfectScoresAchieved get() = preferencesEndpoint.perfectScoresAchieved

    val themeValue get() = preferencesEndpoint.themeValue

    fun setTrainingNotificationSwitchStateTo(state: Boolean) =
        preferencesEndpoint.setTrainingNotificationSwitchStateTo(state)

    fun setTrainingNotificationTimeSetTo(timeSet: Long) =
        preferencesEndpoint.setTrainingNotificationTimeSetTo(timeSet)

    fun incrementPerfectScoresAchieved() =
        preferencesEndpoint.incrementPerfectScoreAchieved()

    fun setThemeValueTo(themeValue: Int) =
        preferencesEndpoint.setThemeValueTo(themeValue)

    fun clearPerfectScoresAchieved() =
        preferencesEndpoint.clearPerfectScoresAchieved()
}