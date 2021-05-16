/*
Copyright 2020 Matheus Menezes

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.mathsemilio.hiraganalearner.others

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.playSFX

class SoundEffectsModule(private val context: Context, private val volume: Float) {

    private val audioAttributes = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .setUsage(AudioAttributes.USAGE_GAME)
        .build()

    private val soundPool = SoundPool.Builder()
        .setMaxStreams(1)
        .setAudioAttributes(audioAttributes)
        .build()

    private var clickSoundEffect = 0
    private var buttonClickSoundEffect = 0
    private var successSoundEffect = 0
    private var errorSoundEffect = 0

    init {
        loadSoundEffects()
    }

    private fun loadSoundEffects() {
        soundPool.apply {
            clickSoundEffect = load(context, R.raw.brandondelehoy_series_of_clicks, 1)
            buttonClickSoundEffect = load(context, R.raw.jaoreir_button_simple_01, 1)
            successSoundEffect = load(context, R.raw.mativve_electro_success_sound, 1)
            errorSoundEffect = load(context, R.raw.autistic_lucario_error, 1)
        }
    }

    fun playClickSoundEffect() = soundPool.playSFX(clickSoundEffect, volume, 1)

    fun playButtonClickSoundEffect() = soundPool.playSFX(buttonClickSoundEffect, volume, 1)

    fun playSuccessSoundEffect() = soundPool.playSFX(successSoundEffect, volume, 1)

    fun playErrorSoundEffect() = soundPool.playSFX(errorSoundEffect, volume, 1)
}