package com.mathsemilio.hiraganalearner.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesRemainingGameTime(context: Context) {

    private val sharedPreferencesAppTheme: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_GAME_TIME, 0)

    private val editor: SharedPreferences.Editor = sharedPreferencesAppTheme.edit()

    fun saveRemainingGameTime(gameTimeRemaining: Long) {
        editor.putLong(TIME_REMAINING, gameTimeRemaining)
        editor.apply()
    }

    fun retrieveGameTimeRemaining(defValue: Long): Long {
        return sharedPreferencesAppTheme.getLong(TIME_REMAINING, defValue)
    }
}