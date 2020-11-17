package com.mathsemilio.hiraganalearner.ui.fragment

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.preference.*
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.data.repository.PreferencesRepository
import com.mathsemilio.hiraganalearner.others.*
import com.mathsemilio.hiraganalearner.others.AlertUser.onClearPerfectScoresPreference
import com.mathsemilio.hiraganalearner.ui.dialogFragment.AppThemeDialogFragment
import java.util.*

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var preferencesRepository: PreferencesRepository
    private lateinit var timePickerDialog: TimePickerDialog
    private lateinit var notificationSwitchPreference: SwitchPreferenceCompat
    private var perfectScoresFromPreference = 0

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_settings, rootKey)

        preferencesRepository = PreferencesRepository(requireContext())

        notificationSwitchPreference = findPreference(TRAINING_NOTIFICATION_PREFERENCE_KEY)!!

        perfectScoresFromPreference = preferencesRepository.getPerfectScoresValue()

        setupPreferences()
    }

    private fun setupPreferences() {
        updateTrainingNotificationState(preferencesRepository.getNotificationSwitchState())

        setNotificationPreferenceSummary(
            preferencesRepository.getNotificationTimeConfigured().formatLongTime(requireContext())
        )

        setGameDefaultDifficultyPreferenceSummaryProvider()

        setupClearPerfectScoresPreference()

        setAppThemePreferenceSummary()

        findPreference<Preference>(APP_BUILD_PREFERENCE_KEY)?.summary = APP_BUILD_VERSION
    }

    private fun updateTrainingNotificationState(stateFromPreference: Boolean) {
        when (stateFromPreference) {
            true -> {
                notificationSwitchPreference.isChecked = true
                updateTrainingNotificationPreferenceTitle()
            }
            false -> {
                notificationSwitchPreference.isChecked = false
                updateTrainingNotificationPreferenceTitle()
            }
        }
    }

    private fun updateTrainingNotificationPreferenceTitle() {
        notificationSwitchPreference.title = when (notificationSwitchPreference.isChecked) {
            true -> getString(R.string.preference_title_training_notification_checked)
            false -> getString(R.string.preference_title_training_notification_unchecked)
        }
    }

    private fun setNotificationPreferenceSummary(formattedTime: String) {
        if (notificationSwitchPreference.isChecked) {
            notificationSwitchPreference.summaryOn = getString(
                R.string.preference_summary_on_training_notification,
                formattedTime
            )
        }
    }

    private fun setGameDefaultDifficultyPreferenceSummaryProvider() {
        findPreference<ListPreference>(DEFAULT_GAME_DIFFICULTY_PREFERENCE_KEY)?.setSummaryProvider {
            return@setSummaryProvider when (PreferenceManager.getDefaultSharedPreferences(
                requireContext()
            ).getString(DEFAULT_GAME_DIFFICULTY_PREFERENCE_KEY, "0")) {
                "0" -> getString(R.string.difficulty_entry_default)
                "1" -> getString(R.string.game_difficulty_beginner)
                "2" -> getString(R.string.game_difficulty_medium)
                "3" -> getString(R.string.game_difficulty_hard)
                else -> throw IllegalArgumentException("Invalid default game difficulty value")
            }
        }
    }

    private fun setupClearPerfectScoresPreference() {
        if (perfectScoresFromPreference == 0) {
            findPreference<Preference>(CLEAR_PERFECT_SCORES_PREFERENCE_KEY)?.isVisible = false
        } else {
            setClearPerfectScoresPreferenceSummaryProvider()
        }
    }

    private fun setClearPerfectScoresPreferenceSummaryProvider() {
        findPreference<Preference>(CLEAR_PERFECT_SCORES_PREFERENCE_KEY)?.setSummaryProvider {
            return@setSummaryProvider getString(
                R.string.preference_summary_clear_perfect_scores,
                perfectScoresFromPreference
            )
        }
    }

    private fun setAppThemePreferenceSummary() {
        findPreference<Preference>(APP_THEME_PREFERENCE_KEY)?.setSummaryProvider {
            return@setSummaryProvider when (preferencesRepository.getAppThemeValue()) {
                0 -> getString(R.string.app_theme_dialog_option_light_theme)
                1 -> getString(R.string.app_theme_dialog_option_dark_theme)
                2 -> getString(R.string.app_theme_dialog_option_follow_system)
                else -> throw IllegalArgumentException("Invalid app theme value")
            }
        }
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            TRAINING_NOTIFICATION_PREFERENCE_KEY -> {
                when (notificationSwitchPreference.isChecked) {
                    true -> {
                        showTimePickerDialog()
                    }
                    false -> {
                        notificationSwitchPreference.title =
                            getString(R.string.preference_title_training_notification_unchecked)

                        NotificationUtil.cancelNotification(requireContext())

                        preferencesRepository.saveNotificationSwitchState(false)
                    }
                }
            }
            CLEAR_PERFECT_SCORES_PREFERENCE_KEY -> {
                onClearPerfectScoresPreference {
                    preferencesRepository.clearPerfectScoresValue()

                    findPreference<Preference>(CLEAR_PERFECT_SCORES_PREFERENCE_KEY)?.isVisible =
                        false
                }
            }
            APP_THEME_PREFERENCE_KEY -> {
                val appThemeDialogFragment = AppThemeDialogFragment()
                appThemeDialogFragment.show(parentFragmentManager, APP_THEME_DIALOG_FRAGMENT_TAG)
            }
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()

        timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                val timeSetByTheUser = calendar.apply {
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }

                if (timeSetByTheUser.timeInMillis > System.currentTimeMillis()) {
                    NotificationUtil
                        .scheduleNotification(
                            requireContext(),
                            timeSetByTheUser.timeInMillis - System.currentTimeMillis()
                        )

                    preferencesRepository.apply {
                        saveNotificationSwitchState(true)
                        saveNotificationTimeConfigured(timeSetByTheUser.timeInMillis)
                    }

                    updateTrainingNotificationPreferenceTitle()

                    setNotificationPreferenceSummary(
                        timeSetByTheUser.timeInMillis.formatLongTime(requireContext())
                    )

                    showToast(
                        getString(R.string.preference_notification_set_toast_message),
                        Toast.LENGTH_SHORT
                    )
                } else {
                    timePickerDialog.cancel()

                    notificationSwitchPreference.isChecked = false

                    preferencesRepository.saveNotificationSwitchState(false)

                    updateTrainingNotificationPreferenceTitle()

                    showToast(
                        getString(R.string.preference_notification_time_in_future_toast_message),
                        Toast.LENGTH_LONG
                    )
                }
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            android.text.format.DateFormat.is24HourFormat(requireContext())
        )

        timePickerDialog.setOnCancelListener {
            notificationSwitchPreference.isChecked = false
        }
        timePickerDialog.setCancelable(false)
        timePickerDialog.setCanceledOnTouchOutside(false)
        timePickerDialog.show()
    }
}