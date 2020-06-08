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
    // checkDarkModeSystemStatus function
    //==========================================================================================
    /**
     * Function to check if any unspecified (or system specific) dark theme is activated,
     * if it is, the isActivated value will be set to true, then returned.
     */
    fun checkDarkModeSystemStatus(): Boolean {
        if (AppCompatDelegate.getDefaultNightMode() ==
            AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
            ||
            AppCompatDelegate.getDefaultNightMode()
            == AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
        ) {
            isActivated = true
        }
        return isActivated.also {
            Log.d(TAG_DARK_MODE_UTIL, "checkDarkModeSystemStatus: isActivated value: $it")
        }
    }

    //==========================================================================================
    // switchDarkModeState function
    //==========================================================================================
    /**
     * Function that sets the application dark theme on/off. It also changes the value of the
     * isActivated variable to represent the dark theme state.
     */
    fun switchDarkModeState() {
        isActivated = if (isActivated) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            false
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            true
        }
    }
}