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
package com.mathsemilio.hiraganalearner.storage.endpoint

import android.content.Context
import android.os.Build
import androidx.preference.PreferenceManager
import com.mathsemilio.hiraganalearner.common.*

class PreferencesEndpoint(context: Context) {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val editor = sharedPreferences.edit()

    val trainingNotificationSwitchState
        get() = sharedPreferences.getBoolean(NOTIFICATION_SWITCH_STATE_KEY, false)

    val trainingNotificationTimeSet
        get() = sharedPreferences.getLong(NOTIFICATION_TIME_SET_KEY, 0L)

    val defaultDifficultyOption
        get() = sharedPreferences.getString(DEFAULT_GAME_DIFFICULTY_PREFERENCE_KEY, "0")!!

    val perfectScoresAchieved
        get() = sharedPreferences.getInt(PERFECT_SCORES_KEY, 0)

    val soundEffectsVolume
        get() = sharedPreferences.getInt(SOUND_EFFECTS_VOLUME_PREFERENCE_KEY, 5).toFloat().div(10F)

    val themeValue
        get() = sharedPreferences.getInt(
            THEME_KEY,
            when (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                true -> 0
                false -> 2
            }
        )

    fun setTrainingNotificationSwitchStateTo(state: Boolean) {
        editor.apply {
            putBoolean(NOTIFICATION_SWITCH_STATE_KEY, state)
            apply()
        }
    }

    fun setTrainingNotificationTimeSetTo(timeSet: Long) {
        editor.apply {
            putLong(NOTIFICATION_TIME_SET_KEY, timeSet)
            apply()
        }
    }

    fun incrementPerfectScoreAchieved() {
        editor.apply {
            putInt(PERFECT_SCORES_KEY, perfectScoresAchieved.inc())
            apply()
        }
    }

    fun setThemeValueTo(themeValue: Int) {
        editor.apply {
            putInt(THEME_KEY, themeValue)
            apply()
        }
    }

    fun clearPerfectScoresAchieved() {
        editor.apply {
            putInt(PERFECT_SCORES_KEY, 0)
            apply()
        }
    }
}