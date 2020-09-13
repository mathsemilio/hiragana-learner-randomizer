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
import com.mathsemilio.hiraganalearner.ui.activity.SettingsActivity.Companion.APP_BUILD_PREF_KEY
import com.mathsemilio.hiraganalearner.ui.activity.SettingsActivity.Companion.APP_BUILD_VERSION
import com.mathsemilio.hiraganalearner.ui.activity.SettingsActivity.Companion.APP_THEME_KEY
import com.mathsemilio.hiraganalearner.ui.activity.SettingsActivity.Companion.CLEAR_PERFECT_SCORES_PREF_KEY
import com.mathsemilio.hiraganalearner.ui.activity.SettingsActivity.Companion.NOTIFICATION_PREF_KEY
import com.mathsemilio.hiraganalearner.ui.activity.SettingsActivity.Companion.TRAINING_NOTIFICATION_TAG
import com.mathsemilio.hiraganalearner.ui.dialogFragment.AppThemeDialogFragment
import com.mathsemilio.hiraganalearner.util.SharedPreferencesPerfectScores
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Activity class that host the Settings Fragment.
 */
class SettingsActivity : AppCompatActivity() {

    companion object {
        const val TRAINING_NOTIFICATION_TAG = "trainingNotification"
        const val APP_BUILD_VERSION = "alpha-1.1"
        const val NOTIFICATION_PREF_KEY = "notification"
        const val CLEAR_PERFECT_SCORES_PREF_KEY = "clearPerfectScores"
        const val APP_THEME_KEY = "appTheme"
        const val APP_BUILD_PREF_KEY = "appBuild"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(toolbar_settings as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout_settings_container, SettingsFragment())
            .commit()
    }
}

/**
 * Fragment class that extends PreferenceFragmentCompat for the Settings screen.
 */
class SettingsFragment : PreferenceFragmentCompat(),
    PreferenceManager.OnPreferenceTreeClickListener {

    private val calendar = Calendar.getInstance()

    //==========================================================================================
    // onCreatePreferences
    //==========================================================================================
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_settings, rootKey)

        updateTrainingNotificationTitle()

        if (SharedPreferencesPerfectScores(requireContext()).retrievePerfectScore() == 0) {
            findPreference<Preference>(CLEAR_PERFECT_SCORES_PREF_KEY)?.isVisible = false
        } else {
            updateClearPerfectScoresNumberPreferenceSummary()
        }

        findPreference<Preference>(APP_BUILD_PREF_KEY)?.summary = APP_BUILD_VERSION
    }

    //==========================================================================================
    // onPreferenceTreeClick
    //==========================================================================================
    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            NOTIFICATION_PREF_KEY -> {
                when (findPreference<SwitchPreferenceCompat>(NOTIFICATION_PREF_KEY)?.isChecked) {
                    true -> {
                        setupTimePickerDialog(calendar)
                    }
                    false -> {
                        cancelTrainingNotification()
                        updateTrainingNotificationTitle()
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
                                .clearPerfectScores()

                            findPreference<Preference>(CLEAR_PERFECT_SCORES_PREF_KEY)
                                ?.isVisible = false
                        })
                    setNegativeButton(
                        getString(R.string.clear_perfect_score_dialog_negative_button_text),
                        null
                    )
                    setCancelable(true)
                    show()
                }
            }
            APP_THEME_KEY -> {
                val appThemeDialogFragment = AppThemeDialogFragment()
                appThemeDialogFragment.show(parentFragmentManager, "AppThemeDialog")
            }
        }

        return super.onPreferenceTreeClick(preference)
    }

    //==========================================================================================
    // setupTimePickerDialog function
    //==========================================================================================
    /**
     * Builds a time picker dialog allowing the user to set the time when the notification
     * should pop up.
     */
    private fun setupTimePickerDialog(calendar: Calendar) {
        val timePicker = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val timeSetByTheUser = calendar.apply {
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }

                if (timeSetByTheUser.timeInMillis > System.currentTimeMillis()) {
                    scheduleTrainingNotification(
                        timeSetByTheUser.timeInMillis - System.currentTimeMillis()
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.training_notification_toast_message_please_select_a_time_in_future),
                        Toast.LENGTH_LONG
                    ).show()
                }

                updateTrainingNotificationTitle()

                Toast.makeText(
                    requireContext(),
                    getString(R.string.preference_notification_set_toast_message),
                    Toast.LENGTH_SHORT
                ).show()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        timePicker.show()
    }

    //==========================================================================================
    // scheduleTrainingNotification function
    //==========================================================================================
    /**
     * Schedules the Work that triggers the training notification based on the initialDelay
     * parameter.
     *
     * @param initialDelay Long that represents the time that the work will be delayed.
     */
    private fun scheduleTrainingNotification(initialDelay: Long) {
        val notificationWork = OneTimeWorkRequestBuilder<NotificationWorkManager>()
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .addTag(TRAINING_NOTIFICATION_TAG)
            .build()

        val workManager = WorkManager.getInstance(requireContext())
        workManager.enqueue(notificationWork)
    }

    //==========================================================================================
    // cancelTrainingNotification function
    //==========================================================================================
    /**
     * Cancels the work that triggers the training notification.
     */
    private fun cancelTrainingNotification() {
        val workManager = WorkManager.getInstance(requireContext())
        workManager.cancelAllWorkByTag(TRAINING_NOTIFICATION_TAG)
    }

    //==========================================================================================
    // updateTrainingNotificationSummary function
    //==========================================================================================
    /**
     * Updates the title for the training notification preference.
     */
    private fun updateTrainingNotificationTitle() {
        when (findPreference<SwitchPreferenceCompat>(NOTIFICATION_PREF_KEY)?.isChecked) {
            true -> findPreference<SwitchPreferenceCompat>(NOTIFICATION_PREF_KEY)?.title =
                getString(R.string.preference_training_notification_title_checked)
            false -> getString(R.string.preference_training_notification_title_unchecked)
        }
    }

    //==========================================================================================
    // updateClearPerfectScoresNumberPreferenceSummary function
    //==========================================================================================
    /**
     * Updates the summary for the clear perfect scores number preference.
     */
    private fun updateClearPerfectScoresNumberPreferenceSummary() {
        findPreference<Preference>(CLEAR_PERFECT_SCORES_PREF_KEY)?.setSummaryProvider {
            return@setSummaryProvider getString(
                R.string.preference_summary_clear_perfect_scores,
                SharedPreferencesPerfectScores(requireContext()).retrievePerfectScore()
            )
        }
    }
}