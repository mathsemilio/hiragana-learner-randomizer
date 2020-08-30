package com.mathsemilio.hiraganalearner.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.mathsemilio.hiraganalearner.R
import kotlinx.android.synthetic.main.activity_settings.*

private const val APP_BUILD_VERSION = "beta-0.8"
private const val APP_BUILD = "app_build"
private const val APP_THEME = "app_theme"
private const val NOTIFICATION = "notification"

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolbarConfig()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_settings, SettingsFragment())
            .commit()
    }

    /**
     * Private function that configures the Toolbar for this activity
     */
    private fun toolbarConfig() {
        setSupportActionBar(toolbar_settings as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}

/**
 * Fragment class for the Settings screen
 */
class SettingsFragment : PreferenceFragmentCompat(),
    PreferenceManager.OnPreferenceTreeClickListener {

    private val appThemePreference = findPreference<ListPreference>(APP_THEME)

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_settings, rootKey)

        findPreference<Preference>(APP_BUILD)?.summary = APP_BUILD_VERSION
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            NOTIFICATION -> {
                TODO("To be implemented")
            }
            APP_THEME -> {
                when (appThemePreference?.value) {
                    "1" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    "2" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }

        return super.onPreferenceTreeClick(preference)
    }
}