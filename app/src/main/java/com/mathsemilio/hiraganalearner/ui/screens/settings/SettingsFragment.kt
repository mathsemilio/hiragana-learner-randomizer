package com.mathsemilio.hiraganalearner.ui.screens.settings

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.commom.*
import com.mathsemilio.hiraganalearner.data.preferences.repository.PreferencesRepository
import com.mathsemilio.hiraganalearner.ui.commom.AlertUserHelper.onClearPerfectScoresPreference
import com.mathsemilio.hiraganalearner.ui.commom.util.buildTimePickerDialog
import com.mathsemilio.hiraganalearner.ui.commom.util.formatLongTime
import com.mathsemilio.hiraganalearner.ui.commom.util.showLongToast
import com.mathsemilio.hiraganalearner.ui.commom.util.showShortToast
import com.mathsemilio.hiraganalearner.ui.dialog.AppThemeDialogFragment
import java.util.*

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var calendar: Calendar
    private lateinit var preferencesRepository: PreferencesRepository
    private lateinit var timePickerDialog: TimePickerDialog
    private lateinit var trainingNotificationSwitchPreference: SwitchPreferenceCompat
    private lateinit var gameDefaultDifficultyOptionsPreference: ListPreference
    private lateinit var clearPerfectScoresPreference: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_settings, rootKey)

        initializeObjects()

        setupClearPerfectScoresPreference()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTrainingNotificationBasedOnPreferenceValue()

        setupDefaultGameDifficultyPreference()

        setupAppThemePreference()

        setupAppBuildVersionPreference()
    }

    private fun initializeObjects() {
        preferencesRepository = PreferencesRepository(requireContext())

        trainingNotificationSwitchPreference =
            findPreference(TRAINING_NOTIFICATION_PREFERENCE_KEY)!!

        gameDefaultDifficultyOptionsPreference =
            findPreference(DEFAULT_GAME_DIFFICULTY_PREFERENCE_KEY)!!

        clearPerfectScoresPreference =
            findPreference(CLEAR_PERFECT_SCORES_PREFERENCE_KEY)!!
    }

    private fun setupTrainingNotificationBasedOnPreferenceValue() {
        when (preferencesRepository.getNotificationSwitchState()) {
            true -> {
                trainingNotificationSwitchPreference.apply {
                    isChecked = true
                    title = getString(R.string.preference_title_training_notification_checked)
                    summaryOn = getString(
                        R.string.preference_summary_on_training_notification,
                        preferencesRepository.getNotificationTimeConfigured()
                            .formatLongTime(requireContext())
                    )
                }
            }
            false -> {
                trainingNotificationSwitchPreference.apply {
                    isChecked = false
                    title = getString(R.string.preference_title_training_notification_unchecked)
                }
            }
        }
    }

    private fun setupDefaultGameDifficultyPreference() {
        gameDefaultDifficultyOptionsPreference.setSummaryProvider {
            return@setSummaryProvider when (preferencesRepository.getGameDefaultOption()) {
                "0" -> getString(R.string.difficulty_entry_default)
                "1" -> getString(R.string.game_difficulty_beginner)
                "2" -> getString(R.string.game_difficulty_medium)
                "3" -> getString(R.string.game_difficulty_hard)
                else -> throw IllegalArgumentException(INVALID_DEFAULT_GAME_OPTION_EXCEPTION)
            }
        }
    }

    private fun setupClearPerfectScoresPreference() {
        if (preferencesRepository.getPerfectScoresValue() == 0)
            clearPerfectScoresPreference.isVisible = false
        else
            clearPerfectScoresPreference.setSummaryProvider {
                return@setSummaryProvider getString(
                    R.string.preference_summary_clear_perfect_scores,
                    preferencesRepository.getPerfectScoresValue()
                )
            }
    }

    private fun setupAppThemePreference() {
        findPreference<Preference>(APP_THEME_PREFERENCE_KEY)?.setSummaryProvider {
            return@setSummaryProvider when (preferencesRepository.getAppThemeValue()) {
                APP_THEME_LIGHT_THEME -> getString(R.string.app_theme_dialog_option_light_theme)
                APP_THEME_DARK_THEME -> getString(R.string.app_theme_dialog_option_dark_theme)
                APP_THEME_FOLLOW_SYSTEM -> getString(R.string.app_theme_dialog_option_follow_system)
                else -> throw IllegalArgumentException(INVALID_APP_THEME_VALUE_EXCEPTION)
            }
        }
    }

    private fun setupAppBuildVersionPreference() {
        findPreference<Preference>(APP_BUILD_PREFERENCE_KEY)?.summary = APP_BUILD_VERSION
    }

    private fun showClearPerfectScoresDialog() {
        onClearPerfectScoresPreference {
            preferencesRepository.clearPerfectScoresValue()
            findPreference<Preference>(CLEAR_PERFECT_SCORES_PREFERENCE_KEY)?.isVisible = false
        }
    }

    private fun handleOnTrainingNotificationPreferenceClick() {
        when (trainingNotificationSwitchPreference.isChecked) {
            true -> showTimePickerDialog()
            false -> {
                trainingNotificationSwitchPreference.title =
                    getString(R.string.preference_title_training_notification_unchecked)

                preferencesRepository.saveNotificationSwitchState(switchState = false)

                TrainingNotificationHelper.cancelNotification(requireContext())
            }
        }
    }

    private fun showTimePickerDialog() {
        calendar = Calendar.getInstance()

        timePickerDialog = buildTimePickerDialog(
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            is24HourView = android.text.format.DateFormat.is24HourFormat(requireContext()),
            { hourSetByUser, minuteSetByUser ->
                handleOnTimeSetByUser(hourSetByUser, minuteSetByUser)
            },
            { trainingNotificationSwitchPreference.isChecked = false }
        )

        timePickerDialog.show()
    }

    private fun handleOnTimeSetByUser(hourSetByUser: Int, minuteSetByUser: Int) {
        val timeSetByUser = calendar.apply {
            set(Calendar.HOUR_OF_DAY, hourSetByUser)
            set(Calendar.MINUTE, minuteSetByUser)
        }

        if (timeSetByUser.timeInMillis > System.currentTimeMillis()) {
            TrainingNotificationHelper.scheduleNotification(
                requireContext(),
                timeSetByUser.timeInMillis - System.currentTimeMillis()
            )

            trainingNotificationSwitchPreference.apply {
                title = getString(R.string.preference_title_training_notification_checked)
                summaryOn = getString(
                    R.string.preference_summary_on_training_notification,
                    timeSetByUser.timeInMillis.formatLongTime(requireContext())
                )
            }

            preferencesRepository.apply {
                saveNotificationTimeConfigured(timeSetByUser.timeInMillis)
                saveNotificationSwitchState(switchState = true)
            }

            showShortToast(getString(R.string.preference_notification_set_toast_message))
        } else {
            timePickerDialog.cancel()

            trainingNotificationSwitchPreference.isChecked = false

            preferencesRepository.saveNotificationSwitchState(switchState = false)

            showLongToast(getString(R.string.preference_notification_time_in_future_toast_message))
        }
    }

    private fun showAppThemeDialog() {
        val appThemeDialog = AppThemeDialogFragment()
        appThemeDialog.show(parentFragmentManager, null)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            TRAINING_NOTIFICATION_PREFERENCE_KEY -> handleOnTrainingNotificationPreferenceClick()
            CLEAR_PERFECT_SCORES_PREFERENCE_KEY -> showClearPerfectScoresDialog()
            APP_THEME_PREFERENCE_KEY -> showAppThemeDialog()
        }
        return super.onPreferenceTreeClick(preference)
    }
}