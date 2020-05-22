package com.mathsemilio.hiraganalearner.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesGameScore (context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        GAME_SCORE_PREF,
        Context.MODE_PRIVATE)

    fun saveGameScore(KEY: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.putInt(KEY, value)
        editor.apply()
    }

    fun retrieveGameScore(KEY: String): Int? {
        return sharedPreferences.getInt(KEY, 0)
    }

    fun clearGameScoreSharedPreferences() {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.clear()
        editor.apply()
    }
}