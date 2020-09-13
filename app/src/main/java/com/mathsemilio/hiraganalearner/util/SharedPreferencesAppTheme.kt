package com.mathsemilio.hiraganalearner.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesAppTheme(context: Context) {

    companion object {
        const val SHARED_PREF_APP_THEME = "appThemeSharedPreferences"
        const val APP_THEME_KEY = "appTheme"
    }

    private val sharedPreferencesAppTheme: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_APP_THEME, 0)

    private val editor: SharedPreferences.Editor = sharedPreferencesAppTheme.edit()

    fun saveThemeValue(value: Int) {
        editor.putInt(APP_THEME_KEY, value)
        editor.apply()
    }

    fun retrieveThemeValue(): Int {
        return sharedPreferencesAppTheme.getInt(APP_THEME_KEY, 0)
    }
}