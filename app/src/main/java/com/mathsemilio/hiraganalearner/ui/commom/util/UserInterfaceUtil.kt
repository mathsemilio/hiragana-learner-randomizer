package com.mathsemilio.hiraganalearner.ui.commom.util

import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Fragment.showMaterialDialog(
    dialogTitle: String,
    dialogMessage: String,
    positiveButtonText: String,
    negativeButtonText: String?,
    isCancelable: Boolean,
    positiveButtonListener: DialogInterface.OnClickListener,
    negativeButtonListener: DialogInterface.OnClickListener?
) {
    MaterialAlertDialogBuilder(requireContext()).apply {
        setTitle(dialogTitle)
        setMessage(dialogMessage)
        setPositiveButton(positiveButtonText, positiveButtonListener)
        setNegativeButton(negativeButtonText, negativeButtonListener)
        setCancelable(isCancelable)
        show()
    }
}

fun Fragment.buildTimePickerDialog(
    currentHour: Int,
    currentMinute: Int,
    is24HourView: Boolean,
    handleOnTimeSet: (hourSetByUser: Int, minuteSetByUser: Int) -> Unit,
    handleOnDialogDismiss: () -> Unit
): TimePickerDialog {
    return TimePickerDialog(
        requireContext(),
        { _, hourOfTheDay, minute -> handleOnTimeSet(hourOfTheDay, minute) },
        currentHour,
        currentMinute,
        is24HourView
    ).apply {
        setOnCancelListener { handleOnDialogDismiss() }
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }
}

fun Fragment.showShortToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showLongToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}

fun Long.formatLongTime(context: Context): String {
    return when (android.text.format.DateFormat.is24HourFormat(context)) {
        true -> android.text.format.DateFormat.format("HH:mm", this).toString()
        false -> android.text.format.DateFormat.format("h:mm a", this).toString()
    }
}