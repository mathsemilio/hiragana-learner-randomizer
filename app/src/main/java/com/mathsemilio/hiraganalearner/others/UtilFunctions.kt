package com.mathsemilio.hiraganalearner.others

import android.content.Context
import android.content.DialogInterface
import android.media.AudioAttributes
import android.media.SoundPool
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Builds a Material Alert Dialog with the specified parameters.
 *
 * @param dialogTitle String for the dialog's title
 * @param dialogMessage String for the dialog's title
 * @param positiveButtonText String for the dialog's positive button text
 * @param negativeButtonText Nullable String for the dialog's negative button text
 * @param cancelable Boolean that determines whether the dialog can be canceled or not
 * @param positiveListener Listener for the positive button
 * @param negativeListener Nullable Listener for the negative button
 */
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

/**
 * Builds and shows a Toast widget with the parameters supplied.
 *
 * @param message String for the toast message to be displayed
 * @param length Int constant from the Toast class that determines how long the toast will be
 * visible.
 */
fun Context.showToast(message: String, length: Int) {
    Toast.makeText(this, message, length).show()
}

/**
 * Sets up and builds a SoundPool object.
 */
fun setupSoundPool(maxStreams: Int): SoundPool {
    val audioAttributes = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .setUsage(AudioAttributes.USAGE_GAME)
        .build()

    return SoundPool.Builder()
        .setMaxStreams(maxStreams)
        .setAudioAttributes(audioAttributes)
        .build()
}