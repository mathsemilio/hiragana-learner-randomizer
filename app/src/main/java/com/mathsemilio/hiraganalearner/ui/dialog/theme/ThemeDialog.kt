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
package com.mathsemilio.hiraganalearner.ui.dialog.theme

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.*
import com.mathsemilio.hiraganalearner.ui.dialog.BaseDialogFragment
import com.mathsemilio.hiraganalearner.ui.common.helper.ThemeHelper

class ThemeDialog : BaseDialogFragment() {

    private lateinit var themeHelper: ThemeHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeHelper = compositionRoot.themeHelper
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it).apply {
                setTitle(getString(R.string.app_theme_dialog_title))
                setSingleChoiceItems(getThemeArray(), getDefaultOption())
                { _, which ->
                    when (which) {
                        0 -> themeHelper.setLightTheme()
                        1 -> themeHelper.setDarkTheme()
                        2 -> themeHelper.setFollowSystemTheme()
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
            when (themeHelper.currentThemeValue) {
                LIGHT_THEME_VALUE -> 0
                DARK_THEME_VALUE -> 1
                else -> throw IllegalArgumentException(ILLEGAL_APP_THEME_VALUE)
            }
        } else {
            when (themeHelper.currentThemeValue) {
                LIGHT_THEME_VALUE -> 0
                DARK_THEME_VALUE -> 1
                FOLLOW_SYSTEM_THEME_VALUE -> 2
                else -> throw IllegalArgumentException(ILLEGAL_APP_THEME_VALUE)
            }
        }
    }
}