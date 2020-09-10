package com.mathsemilio.hiraganalearner.ui.activity

import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mathsemilio.hiraganalearner.NotificationWorkManager
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.util.SharedPreferencesPerfectScores
import com.mathsemilio.hiraganalearner.util.SharedPreferencesTimeSet
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*
import java.util.concurrent.TimeUnit

private const val TRAINING_NOTIFICATION_TAG = "trainingNotification"
private const val APP_BUILD_VERSION = "alpha-1.0"
private const val APP_BUILD_PREF_KEY = "appBuild"
private const val NOTIFICATION_PREF_KEY = "notification"
private const val CLEAR_PERFECT_SCORES_PREF_KEY = "clearPerfectScores"

/**
 * Activity class that hosts the Settings Fragment
 */
class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Setting up the toolbar
        setSupportActionBar(toolbar_settings as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Replacing the frame_layout_settings_container with the SettingsFragment
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout_settings_container, SettingsFragment())
            .commit()
    }
}

/**
 * PreferenceFragment that hosts the settings screen
 */
class SettingsFragment : PreferenceFragmentCompat(),
    PreferenceManager.OnPreferenceTreeClickListener {

    private val calendar = Calendar.getInstance()

    //==========================================================================================
    // onCreatePreferences
    //==========================================================================================
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_settings, rootKey)

        updateNotificationPreferenceSummary()

        if (SharedPreferencesPerfectScores(requireContext()).retrievePerfectScoresNumber() == 0) {
            findPreference<Preference>(CLEAR_PERFECT_SCORES_PREF_KEY)?.isVisible = false
        } else {
            updateClearPerfectScoresNumberPreferenceSummary()
        }

        // Setting up the Summary for the App Build preference
        findPreference<Preference>(APP_BUILD_PREF_KEY)?.summary = APP_BUILD_VERSION
    }

    //==========================================================================================
    // onPreferenceTreeClick
    //==========================================================================================
    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        // Checking the preference's key
        when (preference?.key) {
            NOTIFICATION_PREF_KEY -> {
                when (findPreference<SwitchPreferenceCompat>(NOTIFICATION_PREF_KEY)?.isChecked) {
                    true -> {
                        setupTimePickerDialog(calendar)
                    }
                    false -> {
                        cancelTrainingNotification()
                        SharedPreferencesTimeSet(requireContext()).clearTimeSet()
                    }
                }
            }
            CLEAR_PERFECT_SCORES_PREF_KEY -> {
                MaterialAlertDialogBuilder(requireContext()).apply {
                    setTitle(getString(R.string.clear_perfect_score_dialog_title))
                    setMessage(getString(R.string.clear_perfect_score_dialog_message))
                    setPositiveButton(
                        getString(R.string.clear_perfect_score_dialog_positive_button_text),
                        DialogInterface.OnClickListener { _, _ ->
                            SharedPreferencesPerfectScores(requireContext())
                                .clearPerfectScoresSharedPreferences()

                            updateClearPerfectScoresNumberPreferenceSummary()
                        })
                    setNegativeButton(
                        getString(R.string.clear_perfect_score_dialog_negative_button_text),
                        null
                    )
                    setCancelable(true)
                    show()
                }
            }
        }

        return super.onPreferenceTreeClick(preference)
    }

    //==========================================================================================
    // setupTimePickerDialog function
    //==========================================================================================
    /**
     * Function that builds a time picker dialog for the training notification.
     */
    private fun setupTimePickerDialog(calendar: Calendar) {
        val timePicker = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                // Getting the time set by the user and putting in a calendar object
                val timeSetByTheUser = calendar.apply {
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }

                // Saving the time set by the user in a SharedPreferences object
                SharedPreferencesTimeSet(requireContext()).saveHourSet(hourOfDay)
                SharedPreferencesTimeSet(requireContext()).saveMinuteSet(minute)

                scheduleTrainingNotification(calculateTimeDelayForNotification(timeSetByTheUser))

                updateNotificationPreferenceSummary()

                Toast.makeText(
                    requireContext(),
                    getString(R.string.preference_notification_set_toast_message),
                    Toast.LENGTH_LONG
                ).show()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        timePicker.show()
    }

    private fun scheduleTrainingNotification(delay: Long) {
        val notificationWork = OneTimeWorkRequestBuilder<NotificationWorkManager>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .addTag(TRAINING_NOTIFICATION_TAG)
            .build()

        val workManagerInstance = WorkManager.getInstance(requireContext())
        workManagerInstance.enqueue(notificationWork)
    }

    private fun cancelTrainingNotification() {
        val workManagerInstance = WorkManager.getInstance(requireContext())
        workManagerInstance.cancelAllWorkByTag(TRAINING_NOTIFICATION_TAG)
    }

    private fun calculateTimeDelayForNotification(timeSetByUser: Calendar): Long {
        return timeSetByUser.timeInMillis - System.currentTimeMillis()
    }

    //==========================================================================================
    // updateNotificationPreferenceSummary function
    //==========================================================================================
    /**
     * Function that updates the summary for the training notifications preference, based on
     * it's state.
     */
    private fun updateNotificationPreferenceSummary() {
        findPreference<SwitchPreferenceCompat>(NOTIFICATION_PREF_KEY)?.setSummaryProvider {
            return@setSummaryProvider if (findPreference<SwitchPreferenceCompat>(
                    NOTIFICATION_PREF_KEY
                )?.isChecked!!
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

    //==========================================================================================
    // updateClearPerfectScoresNumberPreferenceSummary function
    //==========================================================================================
    /**
     * Function that updates the summary for the clear perfect scores number preference.
     */
    private fun updateClearPerfectScoresNumberPreferenceSummary() {
        findPreference<Preference>(CLEAR_PERFECT_SCORES_PREF_KEY)?.setSummaryProvider {
            return@setSummaryProvider getString(
                R.string.preference_summary_clear_perfect_scores,
                SharedPreferencesPerfectScores(requireContext()).retrievePerfectScoresNumber()
            )
        }
    }
}