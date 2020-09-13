package com.mathsemilio.hiraganalearner.ui.dialogFragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.util.APP_THEME_DARK_MODE
import com.mathsemilio.hiraganalearner.util.APP_THEME_FOLLOW_SYSTEM
import com.mathsemilio.hiraganalearner.util.APP_THEME_LIGHT_THEME
import com.mathsemilio.hiraganalearner.util.SharedPreferencesAppTheme

class AppThemeDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it).apply {
                setTitle(getString(R.string.app_theme_dialog_title))
                setSingleChoiceItems(R.array.app_theme_array, -1,
                    DialogInterface.OnClickListener { _, which ->
                        when (which) {
                            0 -> {
                                AppCompatDelegate
                                    .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                                SharedPreferencesAppTheme(requireContext()).saveThemeValue(
                                    APP_THEME_FOLLOW_SYSTEM
                                )
                            }
                            1 -> {
                                AppCompatDelegate
                                    .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                                SharedPreferencesAppTheme(requireContext()).saveThemeValue(
                                    APP_THEME_DARK_MODE
                                )
                            }
                            2 -> {
                                AppCompatDelegate
                                    .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                                SharedPreferencesAppTheme(requireContext()).saveThemeValue(
                                    APP_THEME_LIGHT_THEME
                                )
                            }
                        }
                    })
                setNegativeButton(getString(R.string.app_theme_dialog_negative_button_text),
                    DialogInterface.OnClickListener { _, _ ->
                        dialog?.cancel()
                    })
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}