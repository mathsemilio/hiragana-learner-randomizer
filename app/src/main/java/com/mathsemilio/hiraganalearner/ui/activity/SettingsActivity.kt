package com.mathsemilio.hiraganalearner.ui.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.TrainingRemainderBroadcast
import com.mathsemilio.hiraganalearner.util.APP_BUILD
import com.mathsemilio.hiraganalearner.util.APP_BUILD_VERSION
import com.mathsemilio.hiraganalearner.util.NOTIFICATION
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

    private val calendarInstance = Calendar.getInstance()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_settings, rootKey)

        findPreference<Preference>(APP_BUILD)?.summary = APP_BUILD_VERSION
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            NOTIFICATION -> {
                when (findPreference<SwitchPreferenceCompat>(NOTIFICATION)?.isChecked) {
                    true -> {
                        val timePicker = TimePickerDialog(
                            requireContext(),
                            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                                val timeSet = calendarInstance.apply {
                                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                                    set(Calendar.MINUTE, minute)
                                }

                                setupAlarmManager(requireContext(), timeSet)
                            },
                            calendarInstance.get(Calendar.HOUR_OF_DAY),
                            calendarInstance.get(Calendar.MINUTE),
                            false
                        )
                        timePicker.show()
                    }
                    false -> {
                        cancelAlarmManager(requireContext())
                    }
                }
            }
        }

        return super.onPreferenceTreeClick(preference)
    }

    private fun setupAlarmManager(context: Context, timeSet: Calendar) {
        val alarmIntent = Intent(context, TrainingRemainderBroadcast::class.java).let {
            PendingIntent.getBroadcast(
                context,
                1,
                Intent(context, TrainingRemainderBroadcast::class.java),
                0
            )
        }

        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            timeSet.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
    }

    private fun cancelAlarmManager(context: Context) {
        val alarmIntent = Intent(context, TrainingRemainderBroadcast::class.java).let {
            PendingIntent.getBroadcast(
                context,
                1,
                Intent(context, TrainingRemainderBroadcast::class.java),
                0
            )
        }

        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(alarmIntent)
    }
}