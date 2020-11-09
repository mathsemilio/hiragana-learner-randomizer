package com.mathsemilio.hiraganalearner.ui.dialogFragment

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.others.APP_THEME_DARK_MODE
import com.mathsemilio.hiraganalearner.others.APP_THEME_FOLLOW_SYSTEM
import com.mathsemilio.hiraganalearner.others.APP_THEME_LIGHT_THEME
import com.mathsemilio.hiraganalearner.others.AppThemeUtil

/**
 * DialogFragment for the changing the application theme within the Settings fragment.
 */
class AppThemeDialogFragment : DialogFragment() {

    private lateinit var appThemeUtil: AppThemeUtil
    private var appThemeValue = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        appThemeUtil = AppThemeUtil(requireContext())
        appThemeValue = appThemeUtil.getAppThemeValue()

        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it).apply {
                setTitle(getString(R.string.app_theme_dialog_title))
                setSingleChoiceItems(getThemeArray(), getDefaultOption())
                { _, which ->
                    when (which) {
                        0 -> {
                            appThemeUtil.setAppTheme(APP_THEME_LIGHT_THEME)
                        }
                        1 -> {
                            appThemeUtil.setAppTheme(APP_THEME_DARK_MODE)
                        }
                        2 -> {
                            appThemeUtil.setAppTheme(APP_THEME_FOLLOW_SYSTEM)
                        }
                    }
                }
                setNegativeButton(getString(R.string.app_theme_dialog_negative_button_text))
                { _, _ -> dialog?.cancel() }
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun getThemeArray(): Int {
        return when (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            true -> R.array.app_theme_array_sdk_version_below_q
            else -> R.array.app_theme_array
        }
    }

    private fun getDefaultOption(): Int {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            when (appThemeValue) {
                APP_THEME_LIGHT_THEME -> 0
                APP_THEME_DARK_MODE -> 1
                else -> throw IllegalArgumentException("Invalid app theme value")
            }
        } else {
            when (appThemeValue) {
                APP_THEME_LIGHT_THEME -> 0
                APP_THEME_DARK_MODE -> 1
                APP_THEME_FOLLOW_SYSTEM -> 2
                else -> throw IllegalArgumentException("Invalid app theme value")
            }
        }
    }
}