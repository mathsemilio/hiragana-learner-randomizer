package com.mathsemilio.hiraganalearner.others

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesPerfectScores(context: Context) {

    companion object {
        const val SHARED_PREF_PERFECT_SCORES = "sharedPreferencesPerfectScores"
        const val PERFECT_SCORES = "perfectScores"
    }

    private val sharedPreferencesPerfectScores: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_PERFECT_SCORES, 0)

    private val editor: SharedPreferences.Editor = sharedPreferencesPerfectScores.edit()

    /**
     * Increments the perfect score value in the SharedPreferences.
     */
    fun updatePerfectScore() {
        editor.putInt(
            PERFECT_SCORES,
            sharedPreferencesPerfectScores.getInt(PERFECT_SCORES, 0).inc()
        )
        editor.apply()
    }

    /**
     * Retrieves the perfect score value from the SharedPreferences.
     *
     * @return Integer that corresponds the value retrieved from the SharedPreferences
     */
    fun retrievePerfectScore(): Int {
        return sharedPreferencesPerfectScores.getInt(PERFECT_SCORES, 0)
    }

    /**
     * Clears the perfect scores SharedPreferences.
     */
    fun clearPerfectScores() {
        editor.clear()
        editor.apply()
    }
}