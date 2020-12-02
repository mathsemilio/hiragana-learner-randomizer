package com.mathsemilio.hiraganalearner.ui.commom.util

import android.media.AudioAttributes
import android.media.SoundPool

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