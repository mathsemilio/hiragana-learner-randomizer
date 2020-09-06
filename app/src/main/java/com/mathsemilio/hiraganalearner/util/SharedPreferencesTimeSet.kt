package com.mathsemilio.hiraganalearner.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesTimeSet(context: Context) {

    private val sharedPreferencesAppTheme: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_TIME_SET, 0)

    private val editor: SharedPreferences.Editor = sharedPreferencesAppTheme.edit()

    fun saveTimeSet(hourSet: Int, minuteSet: Int, amOrPm: String?) {}
}