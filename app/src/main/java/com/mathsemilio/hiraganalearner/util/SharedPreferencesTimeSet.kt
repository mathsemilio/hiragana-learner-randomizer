package com.mathsemilio.hiraganalearner.util

import android.content.Context
import android.content.SharedPreferences

private const val SHARED_PREF_TIME_SET = "sharedPreferencesTimeSet"
private const val HOUR_SET = "hourSet"
private const val MINUTE_SET = "minuteSet"

class SharedPreferencesTimeSet(context: Context) {

    private val sharedPreferencesTimeSet: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_TIME_SET, 0)

    private val editor: SharedPreferences.Editor = sharedPreferencesTimeSet.edit()

    fun saveHourSet(hourSet: Int) {
        editor.putInt(HOUR_SET, hourSet)
        editor.apply()
    }

    fun saveMinuteSet(minuteSet: Int) {
        editor.putInt(MINUTE_SET, minuteSet)
        editor.apply()
    }

    fun retrieveHourSet(): Int {
        return sharedPreferencesTimeSet.getInt(HOUR_SET, 0)
    }

    fun retrieveMinuteSet(): Int {
        return sharedPreferencesTimeSet.getInt(MINUTE_SET, 0)
    }

    fun clearTimeSet() {
        editor.clear()
        editor.apply()
    }
}