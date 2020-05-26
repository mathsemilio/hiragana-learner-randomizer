package com.mathsemilio.hiraganalearner.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.util.TAG_MAIN_ACTIVITY
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var darkModeActivated: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        image_dark_mode_switch.setOnClickListener { switchDarkMode() }
    }

    private fun switchDarkMode() {
        darkModeActivated = when (darkModeActivated) {
            true -> {
                Log.i(TAG_MAIN_ACTIVITY, "Dark mode deactivated")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                false
            }
            false -> {
                Log.i(TAG_MAIN_ACTIVITY, "Dark mode activated")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                true
            }
        }
    }
}