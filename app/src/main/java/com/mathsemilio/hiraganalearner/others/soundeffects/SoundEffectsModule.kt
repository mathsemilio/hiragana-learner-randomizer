package com.mathsemilio.hiraganalearner.others.soundeffects

import android.content.Context
import android.media.AudioAttributes
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.playSFX

class SoundEffectsModule(private val mContext: Context, private val mVolume: Float) :
    BaseSoundEffectModule() {

    private var mClickSoundEffect = 0
    private var mButtonClickSoundEffect = 0
    private var mSuccessSoundEffect = 0
    private var mErrorSoundEffect = 0

    init {
        setAudioAttributesContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        loadSoundEffects()
    }

    override fun loadSoundEffects() {
        getSoundPool().apply {
            mClickSoundEffect = load(mContext, R.raw.brandondelehoy_series_of_clicks, 1)
            mButtonClickSoundEffect = load(mContext, R.raw.jaoreir_button_simple_01, 1)
            mSuccessSoundEffect = load(mContext, R.raw.mativve_electro_success_sound, 1)
            mErrorSoundEffect = load(mContext, R.raw.autistic_lucario_error, 1)
        }
    }

    fun playClickSoundEffect() = getSoundPool().playSFX(mClickSoundEffect, mVolume, 1)

    fun playButtonClickSoundEffect() = getSoundPool().playSFX(mButtonClickSoundEffect, mVolume, 1)

    fun playSuccessSoundEffect() = getSoundPool().playSFX(mSuccessSoundEffect, mVolume, 1)

    fun playErrorSoundEffect() = getSoundPool().playSFX(mErrorSoundEffect, mVolume, 1)
}