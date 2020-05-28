package com.mathsemilio.hiraganalearner.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.util.TAG_MAIN_ACTIVITY
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //==========================================================================================
    // Class-wide variables
    //==========================================================================================
    // Variable for controlling the dark mode for this activity
    private var darkModeActivated: Boolean = false

    //==========================================================================================
    // onCreate
    //==========================================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // OnClickListener for the imageDarkModeSwitch on the Custom Toolbar, which calls the
        // switchDarkMode function
        image_dark_mode_switch.setOnClickListener { switchDarkMode() }
    }

    //==========================================================================================
    // switchDarkMode function
    //==========================================================================================
    /**
     * Private function that controls when to activate the dark mode. In order to activate/deactivated
     * the dark mode, the AppCompatDelegate constants are used. Both MODE_NIGHT_NO and MODE_NIGHT_YES
     * overrides the OS default dark mode configuration.
     */
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