package com.mathsemilio.hiraganalearner.util

import android.content.Context
import android.content.SharedPreferences

private const val SHARED_PREF_APP_THEME = "appThemeSharedPreferences"
private const val APP_THEME_KEY = "appTheme"

class SharedPreferencesAppTheme(context: Context) {

    private val sharedPreferencesAppTheme: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_APP_THEME, 0)

    private val editor: SharedPreferences.Editor = sharedPreferencesAppTheme.edit()

    fun saveThemeValue(value: String) {
        editor.putString(APP_THEME_KEY, value)
        editor.apply()
    }

    fun retrieveThemeValue(): String {
        return sharedPreferencesAppTheme.getString(APP_THEME_KEY, "0")!!
    }
}