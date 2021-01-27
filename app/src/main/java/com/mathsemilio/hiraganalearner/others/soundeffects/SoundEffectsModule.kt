package com.mathsemilio.hiraganalearner.others.soundeffects

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.playSFX

class SoundEffectsModule(private val context: Context, private val volume: Float) {

    private val mAudioAttributes = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .setUsage(AudioAttributes.USAGE_GAME)
        .build()

    private val mSoundPool = SoundPool.Builder()
        .setMaxStreams(1)
        .setAudioAttributes(mAudioAttributes)
        .build()

    private var mClickSoundEffect = 0
    private var mButtonClickSoundEffect = 0
    private var mSuccessSoundEffect = 0
    private var mErrorSoundEffect = 0

    init {
        loadSoundEffects()
    }

    private fun loadSoundEffects() {
        mSoundPool.apply {
            mClickSoundEffect = load(context, R.raw.brandondelehoy_series_of_clicks, 1)
            mButtonClickSoundEffect = load(context, R.raw.jaoreir_button_simple_01, 1)
            mSuccessSoundEffect = load(context, R.raw.mativve_electro_success_sound, 1)
            mErrorSoundEffect = load(context, R.raw.autistic_lucario_error, 1)
        }
    }

    fun playClickSoundEffect() = mSoundPool.playSFX(mClickSoundEffect, volume, 1)

    fun playButtonClickSoundEffect() = mSoundPool.playSFX(mButtonClickSoundEffect, volume, 1)

    fun playSuccessSoundEffect() = mSoundPool.playSFX(mSuccessSoundEffect, volume, 1)

    fun playErrorSoundEffect() = mSoundPool.playSFX(mErrorSoundEffect, volume, 1)
}