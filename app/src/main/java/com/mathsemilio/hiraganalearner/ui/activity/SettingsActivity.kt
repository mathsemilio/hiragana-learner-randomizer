package com.mathsemilio.hiraganalearner.ui.activity

import android.app.TimePickerDialog
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.mathsemilio.hiraganalearner.AlarmBootBroadcastReceiver
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.util.*
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*

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

class SettingsFragment : PreferenceFragmentCompat(),
    PreferenceManager.OnPreferenceTreeClickListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_settings, rootKey)

        updateNotificationPreferenceSummary()

        findPreference<Preference>(APP_BUILD)?.summary = APP_BUILD_VERSION
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            NOTIFICATION -> {
                when (findPreference<SwitchPreferenceCompat>(NOTIFICATION)?.isChecked) {
                    true -> {
                        val calendarInstance = Calendar.getInstance()
                        val timePicker = TimePickerDialog(
                            requireContext(),
                            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                                val timeSetByTheUser = calendarInstance.apply {
                                    timeInMillis = System.currentTimeMillis()
                                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                                    set(Calendar.MINUTE, minute)
                                }

                                SharedPreferencesTimeSet(requireContext()).saveHourSet(hourOfDay)
                                SharedPreferencesTimeSet(requireContext()).saveMinuteSet(minute)

                                AlarmManagerHelper().setAlarmManager(
                                    requireContext(), timeSetByTheUser
                                )
                                enableAlarmBootBroadcastReceiver()

                                updateNotificationPreferenceSummary()

                                Toast.makeText(
                                    requireContext(),
                                    getString(
                                        R.string.preference_notification_set_toast_message,
                                        SharedPreferencesTimeSet(requireContext()).retrieveHourSet(),
                                        SharedPreferencesTimeSet(requireContext()).retrieveMinuteSet()
                                    ),
                                    Toast.LENGTH_LONG
                                ).show()
                            },
                            calendarInstance.get(Calendar.HOUR_OF_DAY),
                            calendarInstance.get(Calendar.MINUTE),
                            false
                        )
                        timePicker.show()
                    }
                    false -> {
                        AlarmManagerHelper().cancelAlarmManager(requireContext())
                        disableAlarmBootBroadcastReceiver()
                    }
                }
            }
        }

        return super.onPreferenceTreeClick(preference)
    }

    private fun updateNotificationPreferenceSummary() {
        findPreference<SwitchPreferenceCompat>(NOTIFICATION)?.setSummaryProvider {
            return@setSummaryProvider if (findPreference<SwitchPreferenceCompat>(NOTIFICATION)
                    ?.isChecked!!
            ) {
                getString(
                    R.string.preference_training_notification_summary_on,
                    SharedPreferencesTimeSet(requireContext()).retrieveHourSet(),
                    SharedPreferencesTimeSet(requireContext()).retrieveMinuteSet()
                )
            } else {
                getString(R.string.preference_training_notification_summary_off)
            }
        }
    }

    private fun enableAlarmBootBroadcastReceiver() {
        val receiver = ComponentName(requireContext(), AlarmBootBroadcastReceiver::class.java)

        requireContext().packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun disableAlarmBootBroadcastReceiver() {
        val receiver = ComponentName(requireContext(), AlarmBootBroadcastReceiver::class.java)

        requireContext().packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}