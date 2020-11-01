package com.mathsemilio.hiraganalearner.others

import android.content.Context
import android.content.DialogInterface
import android.media.AudioAttributes
import android.media.SoundPool
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.buildMaterialDialog(
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

fun Context.showToast(message: String, length: Int) {
    Toast.makeText(this, message, length).show()
}

fun setupSoundPool(maxAudioStreams: Int): SoundPool {
    val audioAttributes = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .setUsage(AudioAttributes.USAGE_GAME)
        .build()

    return SoundPool.Builder()
        .setMaxStreams(maxAudioStreams)
        .setAudioAttributes(audioAttributes)
        .build()
}

fun SoundPool.playSFX(id: Int, volume: Float, priority: Int) {
    play(id, volume, volume, priority, 0, 1F)
}