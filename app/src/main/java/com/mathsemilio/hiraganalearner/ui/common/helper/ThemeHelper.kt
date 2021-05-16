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
package com.mathsemilio.hiraganalearner.ui.common.helper

import androidx.appcompat.app.AppCompatDelegate
import com.mathsemilio.hiraganalearner.common.DARK_THEME_VALUE
import com.mathsemilio.hiraganalearner.common.FOLLOW_SYSTEM_THEME_VALUE
import com.mathsemilio.hiraganalearner.common.LIGHT_THEME_VALUE
import com.mathsemilio.hiraganalearner.data.manager.PreferencesManager

class ThemeHelper(private val preferencesManager: PreferencesManager) {

    val currentThemeValue get() = preferencesManager.themeValue

    fun setLightTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        preferencesManager.setThemeValueTo(LIGHT_THEME_VALUE)
    }

    fun setDarkTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        preferencesManager.setThemeValueTo(DARK_THEME_VALUE)
    }

    fun setFollowSystemTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        preferencesManager.setThemeValueTo(FOLLOW_SYSTEM_THEME_VALUE)
    }

    fun setThemeFromPreference() {
        when (currentThemeValue) {
            LIGHT_THEME_VALUE ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            DARK_THEME_VALUE ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            FOLLOW_SYSTEM_THEME_VALUE ->
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}