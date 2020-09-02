package com.mathsemilio.hiraganalearner.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.util.APP_BUILD
import com.mathsemilio.hiraganalearner.util.APP_BUILD_VERSION
import com.mathsemilio.hiraganalearner.util.NOTIFICATION
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(toolbar_settings as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_settings, SettingsFragment())
            .commit()
    }
}

/**
 * Fragment class for the Settings screen
 */
class SettingsFragment : PreferenceFragmentCompat(),
    PreferenceManager.OnPreferenceTreeClickListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_settings, rootKey)

        findPreference<Preference>(APP_BUILD)?.summary = APP_BUILD_VERSION
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            NOTIFICATION -> {
                Toast.makeText(requireContext(), "${preference.title} clicked", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return super.onPreferenceTreeClick(preference)
    }
}