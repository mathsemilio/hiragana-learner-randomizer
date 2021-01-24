package com.mathsemilio.hiraganalearner.ui.screens.dialog

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.*
import com.mathsemilio.hiraganalearner.ui.others.AppThemeUtil

class AppThemeDialog : BaseDialogFragment() {

    private lateinit var mAppThemeUtil: AppThemeUtil

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mAppThemeUtil = getCompositionRoot().getAppThemeUtil()

        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it).apply {
                setTitle(getString(R.string.app_theme_dialog_title))
                setSingleChoiceItems(getThemeArray(), getDefaultOption())
                { _, which ->
                    when (which) {
                        0 -> mAppThemeUtil.setLightAppTheme()
                        1 -> mAppThemeUtil.setDarkAppTheme()
                        2 -> mAppThemeUtil.setFollowSystemAppTheme()
                    }
                }
                setNegativeButton(getString(R.string.alert_dialog_cancel_button_text))
                { _, _ -> dialog?.cancel() }
            }
            builder.create()
        } ?: throw IllegalStateException(NULL_ACTIVITY_EXCEPTION)
    }

    private fun getThemeArray(): Int {
        return when (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            true -> R.array.app_theme_array_sdk_version_below_q
            else -> R.array.app_theme_array
        }
    }

    private fun getDefaultOption(): Int {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            when (mAppThemeUtil.getAppThemeValue()) {
                APP_THEME_LIGHT_THEME -> 0
                APP_THEME_DARK_THEME -> 1
                else -> throw IllegalArgumentException(ILLEGAL_APP_THEME_VALUE)
            }
        } else {
            when (mAppThemeUtil.getAppThemeValue()) {
                APP_THEME_LIGHT_THEME -> 0
                APP_THEME_DARK_THEME -> 1
                APP_THEME_FOLLOW_SYSTEM -> 2
                else -> throw IllegalArgumentException(ILLEGAL_APP_THEME_VALUE)
            }
        }
    }
}