package com.mathsemilio.hiraganalearner.others

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesAppTheme(context: Context) {

    companion object {
        const val SHARED_PREF_APP_THEME = "appThemeSharedPreferences"
        const val APP_THEME = "appTheme"
    }

    private val sharedPreferencesAppTheme: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_APP_THEME, 0)

    private val editor: SharedPreferences.Editor = sharedPreferencesAppTheme.edit()

    /**
     * Saves the theme value on the SharedPreferences.
     *
     * @param value Integer that corresponds the value to be saved on the SharedPreferences
     */
    fun saveThemeValue(value: Int) {
        editor.putInt(APP_THEME, value)
        editor.apply()
    }

    /**
     * Retrieves the theme value saved in the SharedPreferences.
     *
     * @return Integer corresponding the theme value saved on the SharedPreferences
     */
    fun retrieveThemeValue(): Int {
        return sharedPreferencesAppTheme.getInt(APP_THEME, 0)
    }
}