package com.mathsemilio.hiraganalearner.ui.commom.util

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.mathsemilio.hiraganalearner.R

fun setupAndLoadInterstitialAd(
    context: Context,
    onAdClosed: () -> Unit
): InterstitialAd {
    return InterstitialAd(context).apply {
        adUnitId = context.getString(R.string.interstitialAdUnitId)
        adListener = object : AdListener() {
            override fun onAdClosed() = onAdClosed()
        }
        loadAd(AdRequest.Builder().build())
    }
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

fun SoundPool.playSFX(
    soundEffectID: Int,
    volume: Float,
    priority: Int
) {
    play(soundEffectID, volume, volume, priority, 0, 1F)
}