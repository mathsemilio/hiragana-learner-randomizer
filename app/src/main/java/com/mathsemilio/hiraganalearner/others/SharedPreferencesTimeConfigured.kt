package com.mathsemilio.hiraganalearner.others

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesTimeConfigured(context: Context) {

    companion object {
        const val TIME_CONFIGURED_SHARED_PREF = "timeConfiguredSharedPreferences"
        const val TIME_CONFIGURED = "timeConfigured"
    }

    private val sharedPreferencesAppTheme: SharedPreferences =
        context.getSharedPreferences(TIME_CONFIGURED_SHARED_PREF, 0)

    private val editor: SharedPreferences.Editor = sharedPreferencesAppTheme.edit()

    fun saveTimeConfigured(timeConfigured: Long) {
        editor.apply {
            putLong(TIME_CONFIGURED, timeConfigured)
            apply()
        }
    }

    fun retrieveTimeConfigured(): Long {
        return sharedPreferencesAppTheme.getLong(TIME_CONFIGURED, 0)
    }
}