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
import com.mathsemilio.hiraganalearner.data.preferences.repository.PreferencesRepository
import com.mathsemilio.hiraganalearner.others.TrainingNotificationHelper
import com.mathsemilio.hiraganalearner.ui.others.DialogHelper
import com.mathsemilio.hiraganalearner.ui.others.MessagesHelper
import com.mathsemilio.hiraganalearner.ui.others.ToolbarVisibilityHelper
import java.util.*

class SettingsScreen : BasePreferenceFragment() {

    private lateinit var mCalendar: Calendar

    private lateinit var mTimePickerDialog: TimePickerDialog

    private lateinit var mTrainingNotificationHelper: TrainingNotificationHelper
    private lateinit var mToolbarVisibilityHelper: ToolbarVisibilityHelper
    private lateinit var mPreferencesRepository: PreferencesRepository
    private lateinit var mMessagesHelper: MessagesHelper
    private lateinit var mDialogHelper: DialogHelper

    private lateinit var mTrainingNotificationSwitchPreference: SwitchPreferenceCompat
    private lateinit var mGameDefaultDifficultyOptionsPreference: ListPreference
    private lateinit var mClearPerfectScoresPreference: Preference

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
        mTrainingNotificationHelper = getCompositionRoot().getTrainingNotificationHelper()

        mToolbarVisibilityHelper = getCompositionRoot().getToolbarVisibilityHelper()

        mPreferencesRepository = getCompositionRoot().getPreferencesRepository()

        mDialogHelper = getCompositionRoot().getDialogHelper()

        mMessagesHelper = getCompositionRoot().getMessagesHelper()

        mTrainingNotificationSwitchPreference =
            findPreference(TRAINING_NOTIFICATION_PREFERENCE_KEY)!!

        mGameDefaultDifficultyOptionsPreference =
            findPreference(DEFAULT_GAME_DIFFICULTY_PREFERENCE_KEY)!!

        mClearPerfectScoresPreference = findPreference(CLEAR_PERFECT_SCORES_PREFERENCE_KEY)!!
    }

    private fun setupTrainingNotificationBasedOnPreferenceValue() {
        when (mPreferencesRepository.getTrainingNotificationSwitchState()) {
            true -> {
                mTrainingNotificationSwitchPreference.apply {
                    isChecked = true
                    title = getString(R.string.preference_title_training_notification_checked)
                    summaryOn = getString(
                        R.string.preference_summary_on_training_notification,
                        mPreferencesRepository.getTrainingNotificationTimeConfigured()
                            .formatLongTime(requireContext())
                    )
                }
            }
            false -> {
                mTrainingNotificationSwitchPreference.apply {
                    isChecked = false
                    title = getString(R.string.preference_title_training_notification_unchecked)
                }
            }
        }
    }

    private fun setupDefaultGameDifficultyPreference() {
        mGameDefaultDifficultyOptionsPreference.setSummaryProvider {
            return@setSummaryProvider when (mPreferencesRepository.getGameDefaultOption()) {
                "0" -> getString(R.string.difficulty_entry_default)
                "1" -> getString(R.string.game_difficulty_beginner)
                "2" -> getString(R.string.game_difficulty_medium)
                "3" -> getString(R.string.game_difficulty_hard)
                else -> throw IllegalArgumentException(ILLEGAL_DEFAULT_DIFFICULTY_VALUE)
            }
        }
    }

    private fun setupClearPerfectScoresPreference() {
        PreferencesRepository(requireContext()).getPerfectScoresValue().also { perfectScores ->
            if (perfectScores == 0)
                mClearPerfectScoresPreference.isVisible = false
            else
                mClearPerfectScoresPreference.setSummaryProvider {
                    return@setSummaryProvider getString(
                        R.string.preference_summary_clear_perfect_scores, perfectScores
                    )
                }
        }
    }

    private fun setupAppThemePreference() {
        findPreference<Preference>(APP_THEME_PREFERENCE_KEY)?.setSummaryProvider {
            return@setSummaryProvider when (mPreferencesRepository.getAppThemeValue()) {
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
        mDialogHelper.showClearPerfectScoresDialog {
            mPreferencesRepository.clearPerfectScoresValue()
            findPreference<Preference>(CLEAR_PERFECT_SCORES_PREFERENCE_KEY)?.isVisible = false
        }

    private fun handleOnTrainingNotificationPreferenceClick() =
        when (mTrainingNotificationSwitchPreference.isChecked) {
            true -> showTimePickerDialog()
            false -> {
                mTrainingNotificationSwitchPreference.title =
                    getString(R.string.preference_title_training_notification_unchecked)

                mPreferencesRepository.setTrainingNotificationSwitchState(false)

                mTrainingNotificationHelper.cancelNotification()
            }
        }

    private fun showTimePickerDialog() {
        mCalendar = Calendar.getInstance()
        mDialogHelper.showTimePickerDialog(
            mCalendar,
            { hourSet, minuteSet -> handleTimeSetByUserEvent(hourSet, minuteSet) },
            { mTrainingNotificationSwitchPreference.isChecked = false }
        ).also { it.show() }
    }

    private fun handleTimeSetByUserEvent(hourSetByUser: Int, minuteSetByUser: Int) {
        val timeSetByUser = mCalendar.apply {
            set(Calendar.HOUR_OF_DAY, hourSetByUser)
            set(Calendar.MINUTE, minuteSetByUser)
        }

        if (timeSetByUser.timeInMillis > System.currentTimeMillis())
            scheduleTrainingReminder(timeSetByUser.timeInMillis)
        else
            scheduleTrainingReminderFailed()
    }

    private fun scheduleTrainingReminder(timeSetByUser: Long) {
        mTrainingNotificationHelper.scheduleNotification(timeSetByUser - System.currentTimeMillis())

        mTrainingNotificationSwitchPreference.apply {
            title = getString(R.string.preference_title_training_notification_checked)
            summaryOn = getString(
                R.string.preference_summary_on_training_notification,
                timeSetByUser.formatLongTime(requireContext())
            )
        }

        mPreferencesRepository.apply {
            setTrainingNotificationSwitchState(true)
            setTrainingNotificationTimeConfigured(timeSetByUser)
        }

        mMessagesHelper.showTrainingReminderSetSuccessfullyMessage()
    }

    private fun scheduleTrainingReminderFailed() {
        mTimePickerDialog.cancel()
        mTrainingNotificationSwitchPreference.isChecked = false
        mPreferencesRepository.setTrainingNotificationSwitchState(false)
        mMessagesHelper.showTrainingReminderSetFailedMessage()
    }

    private fun showAppThemeDialog() {
        mDialogHelper.showAppThemeDialog()
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
        mToolbarVisibilityHelper.setToolbarVisibility(isVisible = true)
        super.onResume()
    }

    override fun onStop() {
        mToolbarVisibilityHelper.setToolbarVisibility(isVisible = false)
        super.onStop()
    }
}