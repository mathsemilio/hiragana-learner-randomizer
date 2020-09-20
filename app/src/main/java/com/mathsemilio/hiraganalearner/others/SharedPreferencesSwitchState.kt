package com.mathsemilio.hiraganalearner.others

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesSwitchState(context: Context) {

    companion object {
        const val SHARED_PREF_SWITCH_STATE = "sharedPreferencesSwitchState"
        const val SWITCH_STATE = "switchState"
    }

    private val sharedPreferencesPerfectScores: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_SWITCH_STATE, 0)

    private val editor: SharedPreferences.Editor = sharedPreferencesPerfectScores.edit()

    /**
     * Saves the state of the SwitchPreferenceCompat for the Training notification.
     *
     * @param state Boolean to represent the Switch state.
     */
    fun saveSwitchState(state: Boolean) {
        editor.putBoolean(SWITCH_STATE, state)
        editor.apply()
    }

    /**
     * Retrieves the Switch state value from the SharedPreferences.
     *
     * @return Boolean value from the SharedPreferences that represents the state of the Switch
     */
    fun retrieveSwitchState(): Boolean {
        return sharedPreferencesPerfectScores.getBoolean(SWITCH_STATE, false)
    }
}