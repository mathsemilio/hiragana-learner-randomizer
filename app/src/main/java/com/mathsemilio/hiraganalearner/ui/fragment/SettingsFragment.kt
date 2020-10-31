package com.mathsemilio.hiraganalearner.ui.fragment

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.preference.*
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.others.*
import com.mathsemilio.hiraganalearner.ui.dialogFragment.AppThemeDialogFragment
import java.util.*
import java.util.concurrent.TimeUnit

class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        const val APP_THEME_DIALOG_TAG = "AppThemeDialog"
        const val TRAINING_NOTIFICATION_TAG = "TrainingNotification"
        const val APP_BUILD_VERSION = "stable-0.1.0"
        const val NOTIFICATION_PREF_KEY = "notification"
        const val CLEAR_PERFECT_SCORES_PREF_KEY = "clearPerfectScores"
        const val APP_THEME_PREF_KEY = "appTheme"
        const val APP_BUILD_PREF_KEY = "appBuild"
    }

    private lateinit var timePickerDialog: TimePickerDialog
    private lateinit var notificationPreference: SwitchPreferenceCompat

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_settings, rootKey)

        notificationPreference = findPreference(NOTIFICATION_PREF_KEY)!!

        updateTrainingNotificationState(
            SharedPreferencesSwitchState(requireContext()).retrieveSwitchState()
        )

        setSummaryForNotificationPreference(
            formatTimeSetByUser(
                SharedPreferencesTimeConfigured(requireContext()).retrieveTimeConfigured()
            )
        )

        setupSummaryProviderForGameDefaultDifficultyPreference()

        setupClearPerfectScoresPreference()

        setupSummaryProviderForAppThemePreference()

        findPreference<Preference>(APP_BUILD_PREF_KEY)?.summary = APP_BUILD_VERSION
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            NOTIFICATION_PREF_KEY -> {
                when (notificationPreference.isChecked) {
                    true -> {
                        setupTimePickerDialog()
                    }
                    false -> {
                        notificationPreference.title =
                            getString(R.string.preference_title_training_notification_unchecked)
                        cancelTrainingNotification()
                        SharedPreferencesSwitchState(requireContext()).saveSwitchState(false)
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
                appThemeDialogFragment.show(parentFragmentManager, APP_THEME_DIALOG_TAG)
            }
        }

        return super.onPreferenceTreeClick(preference)
    }

    private fun updateTrainingNotificationState(state: Boolean) {
        when (state) {
            true -> {
                notificationPreference.isChecked = true
                updateTrainingNotificationPreferenceTitle()
            }
            false -> {
                notificationPreference.isChecked = false
                updateTrainingNotificationPreferenceTitle()
            }
        }
    }

    private fun updateTrainingNotificationPreferenceTitle() {
        notificationPreference.title = when (notificationPreference.isChecked) {
            true -> getString(R.string.preference_title_training_notification_checked)
            false -> getString(R.string.preference_title_training_notification_unchecked)
        }
    }

    private fun setupClearPerfectScoresPreference() {
        if (SharedPreferencesPerfectScores(requireContext()).retrievePerfectScore() == 0) {
            findPreference<Preference>(CLEAR_PERFECT_SCORES_PREF_KEY)?.isVisible = false
        } else {
            setSummaryProviderForClearPerfectScoresPreference()
        }
    }

    private fun setSummaryForNotificationPreference(formattedTime: String) {
        if (notificationPreference.isChecked) {
            notificationPreference.summaryOn = getString(
                R.string.preference_summary_on_training_notification,
                formattedTime
            )
        }
    }

    private fun setupSummaryProviderForGameDefaultDifficultyPreference() {
        findPreference<ListPreference>(DEFAULT_GAME_DIFFICULTY_PREFERENCE_KEY)?.setSummaryProvider {
            return@setSummaryProvider when (PreferenceManager.getDefaultSharedPreferences(
                requireContext()
            ).getString(DEFAULT_GAME_DIFFICULTY_PREFERENCE_KEY, "0")) {
                "0" -> getString(R.string.difficulty_entry_default)
                "1" -> getString(R.string.game_difficulty_beginner)
                "2" -> getString(R.string.game_difficulty_medium)
                else -> getString(R.string.game_difficulty_hard)
            }
        }
    }

    private fun setSummaryProviderForClearPerfectScoresPreference() {
        findPreference<Preference>(CLEAR_PERFECT_SCORES_PREF_KEY)?.setSummaryProvider {
            return@setSummaryProvider getString(
                R.string.preference_summary_clear_perfect_scores,
                SharedPreferencesPerfectScores(requireContext()).retrievePerfectScore()
            )
        }
    }

    private fun setupSummaryProviderForAppThemePreference() {
        findPreference<Preference>(APP_THEME_PREF_KEY)?.setSummaryProvider {
            return@setSummaryProvider when (SharedPreferencesAppTheme(requireContext())
                .retrieveThemeValue()) {
                0 -> getString(R.string.app_theme_dialog_option_light_theme)
                1 -> getString(R.string.app_theme_dialog_option_dark_theme)
                else -> getString(R.string.app_theme_dialog_option_follow_system)
            }
        }
    }

    private fun setupTimePickerDialog() {
        val calendar = Calendar.getInstance()

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

                    updateTrainingNotificationPreferenceTitle()

                    SharedPreferencesTimeConfigured(requireContext()).saveTimeConfigured(
                        timeSetByTheUser.timeInMillis
                    )

                    setSummaryForNotificationPreference(
                        formatTimeSetByUser(
                            timeSetByTheUser.timeInMillis
                        )
                    )

                    requireContext().showToast(
                        getString(R.string.preference_notification_set_toast_message),
                        Toast.LENGTH_SHORT
                    )
                } else {
                    timePickerDialog.cancel()

                    notificationPreference.isChecked = false

                    requireContext().showToast(
                        getString(R.string.preference_notification_time_in_future_toast_message),
                        Toast.LENGTH_LONG
                    )

                    SharedPreferencesSwitchState(requireContext()).saveSwitchState(false)

                    updateTrainingNotificationPreferenceTitle()
                }
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            android.text.format.DateFormat.is24HourFormat(requireContext())
        )
        timePickerDialog.setOnCancelListener { notificationPreference.isChecked = false }

        timePickerDialog.setCancelable(false)
        timePickerDialog.setCanceledOnTouchOutside(false)
        timePickerDialog.show()
    }

    private fun scheduleTrainingNotification(initialDelay: Long) {
        val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .addTag(TRAINING_NOTIFICATION_TAG)
            .build()

        WorkManager.getInstance(requireContext())
            .apply {
                enqueue(notificationWork)
            }
    }

    private fun cancelTrainingNotification() {
        WorkManager.getInstance(requireContext())
            .apply {
                cancelAllWorkByTag(TRAINING_NOTIFICATION_TAG)
            }
    }

    private fun formatTimeSetByUser(timeSetInMillis: Long): String {
        return when (android.text.format.DateFormat.is24HourFormat(requireContext())) {
            true -> android.text.format.DateFormat.format("HH:mm", timeSetInMillis).toString()
            false -> android.text.format.DateFormat.format("h:mm a", timeSetInMillis).toString()
        }
    }
}