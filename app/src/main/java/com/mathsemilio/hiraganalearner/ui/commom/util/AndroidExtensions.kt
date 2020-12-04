package com.mathsemilio.hiraganalearner.ui.commom.util

import android.media.AudioAttributes
import android.media.SoundPool
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd

fun Fragment.setupAndLoadInterstitialAd(
    adUnitId: String,
    handleOnAdClosedEvent: () -> Unit
): InterstitialAd {
    return InterstitialAd(requireContext()).apply {
        setAdUnitId(adUnitId)
        adListener = (object : AdListener() {
            override fun onAdClosed() {
                handleOnAdClosedEvent()
            }
        })
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