package com.mathsemilio.hiraganalearner.util

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate

private const val TAG_DARK_MODE_UTIL = "DarkModeUtil"

/**
 * Singleton responsible for activating/deactivating the app's dark theme
 */
object DarkModeUtil {

    // Variable to represent the dark theme state
    private var isActivated = false

    //==========================================================================================
    // switchDarkModeState function
    //==========================================================================================
    /**
     * Function that sets the application dark theme on/off. It also changes the value of the
     * isActivated variable to represent the dark theme state.
     */
    fun switchDarkModeState() {
        isActivated = if (isActivated) {
            Log.i(TAG_DARK_MODE_UTIL, "switchDarkModeState: Dark theme off")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            false
        } else {
            Log.i(TAG_DARK_MODE_UTIL, "switchDarkModeState: Dark theme on")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            true
        }
    }
}