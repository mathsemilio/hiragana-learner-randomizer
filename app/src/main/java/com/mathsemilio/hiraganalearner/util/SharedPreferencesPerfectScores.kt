package com.mathsemilio.hiraganalearner.util

import android.content.Context
import android.content.SharedPreferences

private const val SHARED_PREF_PERFECT_SCORES = "sharedPreferencesPerfectScores"
private const val PERFECT_SCORES = "perfectScores"

class SharedPreferencesPerfectScores(context: Context) {

    private val sharedPreferencesPerfectScores: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_PERFECT_SCORES, 0)

    private val editor: SharedPreferences.Editor = sharedPreferencesPerfectScores.edit()

    fun updatePerfectScore() {
        editor.putInt(
            PERFECT_SCORES,
            sharedPreferencesPerfectScores.getInt(PERFECT_SCORES, 0).inc()
        )
        editor.apply()
    }

    fun retrievePerfectScoresNumber(): Int {
        return sharedPreferencesPerfectScores.getInt(PERFECT_SCORES, 0)
    }

    fun clearPerfectScoresSharedPreferences() {
        editor.clear()
        editor.apply()
    }
}