package com.mathsemilio.hiraganalearner.ui.fragment

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.mathsemilio.hiraganalearner.BuildConfig
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.others.*
import com.mathsemilio.hiraganalearner.ui.dialogFragment.AppThemeDialogFragment
import java.util.*
import java.util.concurrent.TimeUnit

class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        const val TRAINING_NOTIFICATION_TAG = "trainingNotification"
        const val NOTIFICATION_PREF_KEY = "notification"
        const val CLEAR_PERFECT_SCORES_PREF_KEY = "clearPerfectScores"
        const val APP_THEME_PREF_KEY = "appTheme"
        const val APP_BUILD_PREF_KEY = "appBuild"
    }

    private val calendar = Calendar.getInstance()
    private lateinit var timePickerDialog: TimePickerDialog

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_settings, rootKey)

        updateTrainingNotificationState()

        updateClearPerfectScoresSummary()

        findPreference<Preference>(APP_BUILD_PREF_KEY)?.summary = BuildConfig.VERSION_NAME
    }

    /**
     * Updates the title for the training notification preference based on the switch state.
     */
    private fun updateTrainingNotificationState() {
        if (!SharedPreferencesSwitchState(requireContext()).retrieveSwitchState()) {
            findPreference<SwitchPreferenceCompat>(NOTIFICATION_PREF_KEY)?.isChecked = false
            updateTrainingNotificationTitle()
        } else {
            updateTrainingNotificationTitle()
        }
    }

    /**
     * Updates the summary for the clear perfect scores if the perfect score value saved on
     * the SharedPreferences is not equal to 0, in which case, the preference will be hidden.
     */
    private fun updateClearPerfectScoresSummary() {
        if (SharedPreferencesPerfectScores(requireContext()).retrievePerfectScore() == 0) {
            findPreference<Preference>(CLEAR_PERFECT_SCORES_PREF_KEY)?.isVisible = false
        } else {
            updateClearPerfectScoresNumberPreferenceSummary()
        }
    }

    /**
     * Updates the title for the training notification preference.
     */
    private fun updateTrainingNotificationTitle() {
        when (findPreference<SwitchPreferenceCompat>(NOTIFICATION_PREF_KEY)?.isChecked) {
            true -> findPreference<SwitchPreferenceCompat>(NOTIFICATION_PREF_KEY)?.title =
                getString(R.string.preference_title_training_notification_checked)
            false -> getString(R.string.preference_title_training_notification_unchecked)
        }
    }

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

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            NOTIFICATION_PREF_KEY -> {
                when (findPreference<SwitchPreferenceCompat>(NOTIFICATION_PREF_KEY)?.isChecked) {
                    true -> {
                        setupTimePickerDialog(calendar)
                    }
                    false -> {
                        findPreference<SwitchPreferenceCompat>(NOTIFICATION_PREF_KEY)?.title =
                            getString(R.string.preference_title_training_notification_unchecked)
                        cancelTrainingNotification()
                    }
                }
            }
            CLEAR_PERFECT_SCORES_PREF_KEY -> {
                requireContext().buildMaterialDialog(
                    getString(R.string.clear_perfect_score_dialog_title),
                    getString(R.string.clear_perfect_score_dialog_message),
                    getString(R.string.clear_perfect_score_dialog_positive_button_text),
                    getString(R.string.clear_perfect_score_dialog_negative_button_text),
                    true,
                    { _, _ ->
                        SharedPreferencesPerfectScores(requireContext())
                            .clearPerfectScores()

                        findPreference<Preference>(CLEAR_PERFECT_SCORES_PREF_KEY)
                            ?.isVisible = false
                    },
                    null
                )
            }
            APP_THEME_PREF_KEY -> {
                val appThemeDialogFragment = AppThemeDialogFragment()
                appThemeDialogFragment.show(parentFragmentManager, "AppThemeDialog")
            }
        }

        return super.onPreferenceTreeClick(preference)
    }

    /**
     * Sets up the Time Picker dialog for the Training notification.
     *
     * @param calendar Calendar instance required for the dialog
     */
    private fun setupTimePickerDialog(calendar: Calendar) {
        timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                val timeSetByTheUser = calendar.apply {
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }

                if (timeSetByTheUser.timeInMillis > System.currentTimeMillis()) {
                    scheduleTrainingNotification(
                        timeSetByTheUser.timeInMillis - System.currentTimeMillis()
                    )

                    SharedPreferencesSwitchState(requireContext()).saveSwitchState(true)

                    updateTrainingNotificationTitle()

                    requireContext().showToast(
                        getString(R.string.preference_notification_set_toast_message),
                        Toast.LENGTH_SHORT
                    )
                } else {
                    findPreference<SwitchPreferenceCompat>(NOTIFICATION_PREF_KEY)?.isChecked =
                        false

                    timePickerDialog.cancel()

                    requireContext().showToast(
                        getString(R.string.preference_notification_time_in_future_toast_message),
                        Toast.LENGTH_LONG
                    )
                }
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        timePickerDialog.show()
    }

    /**
     * Schedules the Work that triggers the training notification.
     *
     * @param initialDelay Long that represents the amount of time the work will be delayed.
     */
    private fun scheduleTrainingNotification(initialDelay: Long) {
        val notificationWork = OneTimeWorkRequestBuilder<NotificationWorkManager>()
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .addTag(TRAINING_NOTIFICATION_TAG)
            .build()

        val workManager = WorkManager.getInstance(requireContext())
        workManager.enqueue(notificationWork)
    }


    /**
     * Cancels the work that triggers the training notification.
     */
    private fun cancelTrainingNotification() {
        val workManager = WorkManager.getInstance(requireContext())
        workManager.cancelAllWorkByTag(TRAINING_NOTIFICATION_TAG)
    }
}