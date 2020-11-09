package com.mathsemilio.hiraganalearner.others

import android.content.Context
import android.content.DialogInterface
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.dialog.MaterialAlertDialogBuilder

//==========================================================================================
// Utility functions to build UI elements
//==========================================================================================
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

fun Fragment.showToast(message: String, length: Int) {
    Toast.makeText(requireContext(), message, length).show()
}

//==========================================================================================
// Utility functions for SoundPool
//==========================================================================================
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

fun SoundPool.playSFX(id: Int, volume: Float, priority: Int) =
    play(id, volume, volume, priority, 0, 1F)

//==========================================================================================
// Others
//==========================================================================================
fun Context.setupAndLoadInterstitialAd(
    adUnitId: String,
    actionToBePerformedWhenAdClosed: () -> Unit
): InterstitialAd {
    return InterstitialAd(this).apply {
        setAdUnitId(adUnitId)
        adListener = (object : AdListener() {
            override fun onAdClosed() {
                actionToBePerformedWhenAdClosed.invoke()
            }
        })
        loadAd(AdRequest.Builder().build())
    }
}

fun Long.formatLongTime(context: Context): String {
    return when (android.text.format.DateFormat.is24HourFormat(context)) {
        true -> android.text.format.DateFormat.format("HH:mm", this).toString()
        false -> android.text.format.DateFormat.format("h:mm a", this).toString()
    }
}

fun getAppDefaultThemeValue(): Int {
    return when {
        Build.VERSION.SDK_INT < Build.VERSION_CODES.Q -> 0
        else -> 2
    }
}