/*
Copyright 2020 Matheus Menezes

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.mathsemilio.hiraganalearner.ui.screens.settings

import android.os.Bundle
import android.view.View
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.SwitchPreferenceCompat
import com.mathsemilio.hiraganalearner.BuildConfig
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.*
import com.mathsemilio.hiraganalearner.common.eventbus.EventListener
import com.mathsemilio.hiraganalearner.common.eventbus.EventSubscriber
import com.mathsemilio.hiraganalearner.data.manager.PreferencesManager
import com.mathsemilio.hiraganalearner.others.notification.TrainingNotificationScheduler
import com.mathsemilio.hiraganalearner.ui.common.event.PromptDialogEvent
import com.mathsemilio.hiraganalearner.ui.common.event.TimePickerDialogEvent
import com.mathsemilio.hiraganalearner.ui.common.helper.ToolbarVisibilityHelper
import com.mathsemilio.hiraganalearner.ui.common.manager.DialogManager
import com.mathsemilio.hiraganalearner.ui.common.manager.MessagesManager
import com.mathsemilio.hiraganalearner.ui.dialog.timepicker.TimePickerDialog
import java.util.*

class SettingsFragment : BasePreferenceFragment(),
    TrainingNotificationScheduler.Listener,
    EventListener {

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }

    private lateinit var timePickerDialog: TimePickerDialog

    private lateinit var trainingNotificationScheduler: TrainingNotificationScheduler
    private lateinit var toolbarVisibilityHelper: ToolbarVisibilityHelper
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var messagesManager: MessagesManager
    private lateinit var dialogManager: DialogManager

    private lateinit var eventSubscriber: EventSubscriber

    private lateinit var trainingNotificationPreference: SwitchPreferenceCompat
    private lateinit var gameDefaultDifficultyPreference: ListPreference
    private lateinit var clearPerfectScoresPreference: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_settings, rootKey)

        preferencesManager = compositionRoot.preferencesManager

        clearPerfectScoresPreference = findPreference(CLEAR_PERFECT_SCORES_PREFERENCE_KEY)!!

        setupClearPerfectScoresPreference()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialize()

        setupTrainingNotificationPreference()

        setupDefaultGameDifficultyPreference()

        setupAppThemePreference()

        findPreference<Preference>(APP_BUILD_PREFERENCE_KEY)?.summary = BuildConfig.VERSION_NAME
    }

    private fun initialize() {
        trainingNotificationScheduler = compositionRoot.trainingNotificationScheduler
        toolbarVisibilityHelper = compositionRoot.toolbarVisibilityHelper
        messagesManager = compositionRoot.messagesManager
        dialogManager = compositionRoot.dialogManager
        eventSubscriber = compositionRoot.eventSubscriber
        trainingNotificationPreference = findPreference(TRAINING_NOTIFICATION_PREFERENCE_KEY)!!
        gameDefaultDifficultyPreference = findPreference(DEFAULT_GAME_DIFFICULTY_PREFERENCE_KEY)!!
    }

    private fun setupTrainingNotificationPreference() {
        when (preferencesManager.trainingNotificationSwitchState) {
            true -> {
                trainingNotificationPreference.apply {
                    isChecked = true
                    title = getString(R.string.preference_title_training_notification_checked)
                    summaryOn = getString(
                        R.string.preference_summary_on_training_notification,
                        preferencesManager.trainingNotificationTimeSet
                            .formatTimeInMillis(requireContext())
                    )
                }
            }
            false -> {
                trainingNotificationPreference.apply {
                    isChecked = false
                    title = getString(R.string.preference_title_training_notification_unchecked)
                }
            }
        }
    }

    private fun setupDefaultGameDifficultyPreference() {
        gameDefaultDifficultyPreference.setSummaryProvider {
            return@setSummaryProvider when (preferencesManager.defaultDifficultyValue) {
                SHOW_DIFFICULTY_OPTIONS -> getString(R.string.difficulty_entry_default)
                DEFAULT_DIFFICULTY_BEGINNER -> getString(R.string.game_difficulty_beginner)
                DEFAULT_DIFFICULTY_MEDIUM -> getString(R.string.game_difficulty_medium)
                DEFAULT_DIFFICULTY_HARD -> getString(R.string.game_difficulty_hard)
                else -> throw IllegalArgumentException(ILLEGAL_DEFAULT_DIFFICULTY_VALUE)
            }
        }
    }

    private fun setupClearPerfectScoresPreference() {
        preferencesManager.perfectScoresAchieved.also { perfectScoresAchieved ->
            if (perfectScoresAchieved == 0)
                clearPerfectScoresPreference.isVisible = false
            else
                clearPerfectScoresPreference.setSummaryProvider {
                    return@setSummaryProvider getString(
                        R.string.preference_summary_clear_perfect_scores, perfectScoresAchieved
                    )
                }
        }
    }

    private fun setupAppThemePreference() {
        findPreference<Preference>(THEME_PREFERENCE_KEY)?.setSummaryProvider {
            return@setSummaryProvider when (preferencesManager.themeValue) {
                LIGHT_THEME_VALUE -> getString(R.string.app_theme_dialog_option_light_theme)
                DARK_THEME_VALUE -> getString(R.string.app_theme_dialog_option_dark_theme)
                FOLLOW_SYSTEM_THEME_VALUE -> getString(R.string.app_theme_dialog_option_follow_system)
                else -> throw IllegalArgumentException(ILLEGAL_APP_THEME_VALUE)
            }
        }
    }

    private fun onTrainingNotificationPreferenceClick() =
        when (trainingNotificationPreference.isChecked) {
            true -> timePickerDialog = dialogManager.showTimePickerDialog()
            false -> {
                trainingNotificationPreference.title =
                    getString(R.string.preference_title_training_notification_unchecked)

                preferencesManager.setTrainingNotificationSwitchStateTo(false)

                trainingNotificationScheduler.cancelNotification()
            }
        }

    override fun onTrainingNotificationScheduledSuccessfully(timeSetByUser: Long) {
        trainingNotificationPreference.apply {
            title = getString(R.string.preference_title_training_notification_checked)
            summaryOn = getString(
                R.string.preference_summary_on_training_notification,
                timeSetByUser.formatTimeInMillis(requireContext())
            )
        }

        preferencesManager.setTrainingNotificationSwitchStateTo(true)
        preferencesManager.setTrainingNotificationTimeSetTo(timeSetByUser)

        messagesManager.showTrainingReminderSetSuccessfullyMessage()
    }

    override fun onTrainingNotificationInvalidTimeSet() {
        timePickerDialog.dismiss()

        trainingNotificationPreference.isChecked = false

        preferencesManager.setTrainingNotificationSwitchStateTo(false)

        messagesManager.showTrainingReminderSetFailedMessage()
    }

    override fun onEvent(event: Any) {
        when (event) {
            is TimePickerDialogEvent -> handleTimePickerDialogEvent(event)
            is PromptDialogEvent -> handleClearPerfectScoresPromptDialogEvent(event)
        }
    }

    private fun handleTimePickerDialogEvent(event: TimePickerDialogEvent) {
        when (event) {
            is TimePickerDialogEvent.TimeSet ->
                trainingNotificationScheduler.checkTimeSetAndSchedule(event.timeSetInMillis)
            TimePickerDialogEvent.Dismissed ->
                trainingNotificationPreference.isChecked = false
        }
    }

    private fun handleClearPerfectScoresPromptDialogEvent(event: PromptDialogEvent) {
        when (event) {
            PromptDialogEvent.PositiveButtonClicked -> {
                preferencesManager.clearPerfectScoresAchieved()
                findPreference<Preference>(CLEAR_PERFECT_SCORES_PREFERENCE_KEY)?.isVisible = false
            }
            PromptDialogEvent.NegativeButtonClicked -> {
                // No action to be performed upon negative button click - no-op
            }
        }
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            TRAINING_NOTIFICATION_PREFERENCE_KEY -> onTrainingNotificationPreferenceClick()
            CLEAR_PERFECT_SCORES_PREFERENCE_KEY -> dialogManager.showClearPerfectScoresDialog()
            THEME_PREFERENCE_KEY -> dialogManager.showAppThemeDialog()
        }
        return super.onPreferenceTreeClick(preference)
    }

    override fun onStart() {
        super.onStart()
        trainingNotificationScheduler.addListener(this)
        eventSubscriber.subscribe(this)
    }

    override fun onResume() {
        super.onResume()
        toolbarVisibilityHelper.showToolbar()
    }

    override fun onStop() {
        super.onStop()
        eventSubscriber.unsubscribe(this)
        toolbarVisibilityHelper.hideToolbar()
        trainingNotificationScheduler.removeListener(this)
    }
}