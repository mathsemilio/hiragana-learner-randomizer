package com.mathsemilio.hiraganalearner.common

import android.content.Context
import android.content.DialogInterface
import android.media.SoundPool
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.showMaterialDialog(
    dialogTitle: String,
    dialogMessage: String,
    positiveButtonText: String,
    negativeButtonText: String?,
    isCancelable: Boolean,
    positiveButtonListener: DialogInterface.OnClickListener,
    negativeButtonListener: DialogInterface.OnClickListener?
) {
    MaterialAlertDialogBuilder(this).apply {
        setTitle(dialogTitle)
        setMessage(dialogMessage)
        setPositiveButton(positiveButtonText, positiveButtonListener)
        setNegativeButton(negativeButtonText, negativeButtonListener)
        setCancelable(isCancelable)
        show()
    }
}

fun SoundPool.playSFX(soundEffectID: Int, volume: Float, priority: Int) {
    play(soundEffectID, volume, volume, priority, 0, 1F)
}

fun Long.formatTimeInMillis(context: Context): String {
    return when (android.text.format.DateFormat.is24HourFormat(context)) {
        true -> android.text.format.DateFormat.format("HH:mm", this).toString()
        false -> android.text.format.DateFormat.format("h:mm a", this).toString()
    }
}