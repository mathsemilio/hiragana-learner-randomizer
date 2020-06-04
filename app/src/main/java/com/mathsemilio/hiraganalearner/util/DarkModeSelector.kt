package com.mathsemilio.hiraganalearner.util

import androidx.appcompat.app.AppCompatDelegate

object DarkModeSelector {

    var isActivated = false

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