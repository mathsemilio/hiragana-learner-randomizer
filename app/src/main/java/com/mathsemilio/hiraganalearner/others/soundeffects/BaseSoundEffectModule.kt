package com.mathsemilio.hiraganalearner.others.soundeffects

import android.media.AudioAttributes
import android.media.SoundPool

abstract class BaseSoundEffectModule {

    private var mContentType = AudioAttributes.CONTENT_TYPE_UNKNOWN

    private val mAudioAttributes = AudioAttributes.Builder()
        .setContentType(mContentType)
        .setUsage(AudioAttributes.USAGE_GAME)
        .build()

    private val mSoundPool = SoundPool.Builder()
        .setMaxStreams(1)
        .setAudioAttributes(mAudioAttributes)
        .build()

    protected fun setAudioAttributesContentType(contentType: Int) { mContentType = contentType }

    protected fun getSoundPool(): SoundPool = mSoundPool

    protected abstract fun loadSoundEffects()
}