package com.mathsemilio.hiraganalearner.others.soundeffects

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.playSFX

class SoundEffectsModule(private val mContext: Context, private val mVolume: Float) {

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
            mClickSoundEffect = load(mContext, R.raw.brandondelehoy_series_of_clicks, 1)
            mButtonClickSoundEffect = load(mContext, R.raw.jaoreir_button_simple_01, 1)
            mSuccessSoundEffect = load(mContext, R.raw.mativve_electro_success_sound, 1)
            mErrorSoundEffect = load(mContext, R.raw.autistic_lucario_error, 1)
        }
    }

    fun playClickSoundEffect() = mSoundPool.playSFX(mClickSoundEffect, mVolume, 1)

    fun playButtonClickSoundEffect() = mSoundPool.playSFX(mButtonClickSoundEffect, mVolume, 1)

    fun playSuccessSoundEffect() = mSoundPool.playSFX(mSuccessSoundEffect, mVolume, 1)

    fun playErrorSoundEffect() = mSoundPool.playSFX(mErrorSoundEffect, mVolume, 1)
}