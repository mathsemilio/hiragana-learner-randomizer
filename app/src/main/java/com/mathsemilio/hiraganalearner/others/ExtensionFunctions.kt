package com.mathsemilio.hiraganalearner.others

import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.buildMaterialDialog(
    dialogTitle: String, dialogMessage: String,
    positiveButtonText: String, negativeButtonText: String?,
    cancelable: Boolean,
    positiveListener: DialogInterface.OnClickListener,
    negativeListener: DialogInterface.OnClickListener?
) {
    MaterialAlertDialogBuilder(this).apply {
        setTitle(dialogTitle)
        setMessage(dialogMessage)
        setPositiveButton(positiveButtonText, positiveListener)
        setNegativeButton(negativeButtonText, negativeListener)
        setCancelable(cancelable)
        show()
    }
}

fun Context.showToast(message: String, length: Int) {
    Toast.makeText(this, message, length).show()
}