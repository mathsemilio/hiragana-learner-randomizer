package com.mathsemilio.hiraganalearner.ui.screens.settings

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.SwitchPreferenceCompat
import com.mathsemilio.hiraganalearner.BuildConfig
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.*
import com.mathsemilio.hiraganalearner.common.event.ToolbarVisibilityChangedEvent
import com.mathsemilio.hiraganalearner.common.event.poster.EventPoster
import com.mathsemilio.hiraganalearner.data.repository.PreferencesRepository
import com.mathsemilio.hiraganalearner.others.notification.TrainingNotificationHelper
import com.mathsemilio.hiraganalearner.ui.common.helper.DialogHelper
import com.mathsemilio.hiraganalearner.ui.common.helper.MessagesHelper
import java.util.*

class SettingsScreen : BasePreferenceFragment() {

    private lateinit var calendar: Calendar

    private lateinit var timePickerDialog: TimePickerDialog

    private lateinit var trainingNotificationHelper: TrainingNotificationHelper
    private lateinit var preferencesRepository: PreferencesRepository
    private lateinit var messagesHelper: MessagesHelper
    private lateinit var dialogHelper: DialogHelper
    private lateinit var eventPoster: EventPoster

    private lateinit var trainingNotificationSwitchPreference: SwitchPreferenceCompat
    private lateinit var gameDefaultDifficultyOptionsPreference: ListPreference
    private lateinit var clearPerfectScoresPreference: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_settings, rootKey)

        preferencesRepository = compositionRoot.preferencesRepository

        clearPerfectScoresPreference = findPreference(CLEAR_PERFECT_SCORES_PREFERENCE_KEY)!!

        setupClearPerfectScoresPreference()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialize()

        setupTrainingNotificationBasedOnPreferenceValue()

        setupDefaultGameDifficultyPreference()

        setupAppThemePreference()

        setupAppBuildVersionPreference()
    }

    private fun initialize() {
        trainingNotificationHelper = compositionRoot.trainingNotificationHelper

        messagesHelper = compositionRoot.messagesHelper

        dialogHelper = compositionRoot.dialogHelper

        eventPoster = compositionRoot.eventPoster

        trainingNotificationSwitchPreference =
            findPreference(TRAINING_NOTIFICATION_PREFERENCE_KEY)!!

        gameDefaultDifficultyOptionsPreference =
            findPreference(DEFAULT_GAME_DIFFICULTY_PREFERENCE_KEY)!!
    }

    private fun setupTrainingNotificationBasedOnPreferenceValue() {
        when (preferencesRepository.trainingNotificationSwitchState) {
            true -> {
                trainingNotificationSwitchPreference.apply {
                    isChecked = true
                    title = getString(R.string.preference_title_training_notification_checked)
                    summaryOn = getString(
                        R.string.preference_summary_on_training_notification,
                        preferencesRepository.trainingNotificationTimeConfigured
                            .formatTimeInMillis(requireContext())
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
            return@setSummaryProvider when (preferencesRepository.gameDefaultOption) {
                "0" -> getString(R.string.difficulty_entry_default)
                "1" -> getString(R.string.game_difficulty_beginner)
                "2" -> getString(R.string.game_difficulty_medium)
                "3" -> getString(R.string.game_difficulty_hard)
                else -> throw IllegalArgumentException(ILLEGAL_DEFAULT_DIFFICULTY_VALUE)
            }
        }
    }

    private fun setupClearPerfectScoresPreference() {
        preferencesRepository.perfectScoresValue.also { perfectScores ->
            if (perfectScores == 0)
                clearPerfectScoresPreference.isVisible = false
            else
                clearPerfectScoresPreference.setSummaryProvider {
                    return@setSummaryProvider getString(
                        R.string.preference_summary_clear_perfect_scores, perfectScores
                    )
                }
        }
    }

    private fun setupAppThemePreference() {
        findPreference<Preference>(APP_THEME_PREFERENCE_KEY)?.setSummaryProvider {
            return@setSummaryProvider when (preferencesRepository.appThemeValue) {
                APP_THEME_LIGHT_THEME -> getString(R.string.app_theme_dialog_option_light_theme)
                APP_THEME_DARK_THEME -> getString(R.string.app_theme_dialog_option_dark_theme)
                APP_THEME_FOLLOW_SYSTEM -> getString(R.string.app_theme_dialog_option_follow_system)
                else -> throw IllegalArgumentException(ILLEGAL_APP_THEME_VALUE)
            }
        }
    }

    private fun setupAppBuildVersionPreference() {
        findPreference<Preference>(APP_BUILD_PREFERENCE_KEY)?.summary = BuildConfig.VERSION_NAME
    }

    private fun showClearPerfectScoresDialog() =
        dialogHelper.showClearPerfectScoresDialog {
            preferencesRepository.clearPerfectScoresValue()
            findPreference<Preference>(CLEAR_PERFECT_SCORES_PREFERENCE_KEY)?.isVisible = false
        }

    private fun handleOnTrainingNotificationPreferenceClick() =
        when (trainingNotificationSwitchPreference.isChecked) {
            true -> showTimePickerDialog()
            false -> {
                trainingNotificationSwitchPreference.title =
                    getString(R.string.preference_title_training_notification_unchecked)

                preferencesRepository.setTrainingNotificationSwitchState(false)

                trainingNotificationHelper.cancelNotification()
            }
        }

    private fun showTimePickerDialog() {
        calendar = Calendar.getInstance()
        dialogHelper.showTimePickerDialog(
            calendar,
            { hourSet, minuteSet -> handleTimeSetByUserEvent(hourSet, minuteSet) },
            { trainingNotificationSwitchPreference.isChecked = false }
        ).also { it.show() }
    }

    private fun handleTimeSetByUserEvent(hourSetByUser: Int, minuteSetByUser: Int) {
        val timeSetByUser = calendar.apply {
            set(Calendar.HOUR_OF_DAY, hourSetByUser)
            set(Calendar.MINUTE, minuteSetByUser)
        }

        if (timeSetByUser.timeInMillis > System.currentTimeMillis())
            scheduleTrainingReminder(timeSetByUser.timeInMillis)
        else
            scheduleTrainingReminderFailed()
    }

    private fun scheduleTrainingReminder(timeSetByUser: Long) {
        trainingNotificationHelper.scheduleNotification(timeSetByUser - System.currentTimeMillis())

        trainingNotificationSwitchPreference.apply {
            title = getString(R.string.preference_title_training_notification_checked)
            summaryOn = getString(
                R.string.preference_summary_on_training_notification,
                timeSetByUser.formatTimeInMillis(requireContext())
            )
        }

        preferencesRepository.apply {
            setTrainingNotificationSwitchState(true)
            setTrainingNotificationTimeConfigured(timeSetByUser)
        }

        messagesHelper.showTrainingReminderSetSuccessfullyMessage()
    }

    private fun scheduleTrainingReminderFailed() {
        timePickerDialog.cancel()
        trainingNotificationSwitchPreference.isChecked = false
        preferencesRepository.setTrainingNotificationSwitchState(false)
        messagesHelper.showTrainingReminderSetFailedMessage()
    }

    private fun showAppThemeDialog() {
        dialogHelper.showAppThemeDialog()
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            TRAINING_NOTIFICATION_PREFERENCE_KEY -> handleOnTrainingNotificationPreferenceClick()
            CLEAR_PERFECT_SCORES_PREFERENCE_KEY -> showClearPerfectScoresDialog()
            APP_THEME_PREFERENCE_KEY -> showAppThemeDialog()
        }
        return super.onPreferenceTreeClick(preference)
    }

    override fun onResume() {
        eventPoster.postEvent(ToolbarVisibilityChangedEvent(ToolbarVisibilityChangedEvent.Event.SHOW_TOOLBAR))
        super.onResume()
    }

    override fun onStop() {
        eventPoster.postEvent(ToolbarVisibilityChangedEvent(ToolbarVisibilityChangedEvent.Event.HIDE_TOOLBAR))
        super.onStop()
    }
}