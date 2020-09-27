package com.mathsemilio.hiraganalearner.others

import android.content.Context
import android.content.SharedPreferences
import android.os.Build

class SharedPreferencesAppTheme(context: Context) {

    companion object {
        const val SHARED_PREF_APP_THEME = "appThemeSharedPreferences"
        const val APP_THEME = "appTheme"
        const val DEFAULT_VALUE_BELOW_Q = 0
        const val DEFAULT_VALUE_Q = 2
    }

    private val sharedPreferencesAppTheme: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_APP_THEME, 0)

    private val editor: SharedPreferences.Editor = sharedPreferencesAppTheme.edit()

    fun saveThemeValue(value: Int) {
        editor.apply {
            putInt(APP_THEME, value)
            apply()
        }
    }

    fun retrieveThemeValue(): Int {
        return when (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            true -> sharedPreferencesAppTheme.getInt(APP_THEME, DEFAULT_VALUE_BELOW_Q)
            false -> sharedPreferencesAppTheme.getInt(APP_THEME, DEFAULT_VALUE_Q)
        }
    }
}