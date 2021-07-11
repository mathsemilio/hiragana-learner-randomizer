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

package com.mathsemilio.hiraganalearner.ui.screens.main

import com.mathsemilio.hiraganalearner.databinding.GameMainScreenBinding
import com.mathsemilio.hiraganalearner.ui.common.view.BaseObservableView

abstract class GameMainScreenView :
    BaseObservableView<GameMainScreenBinding, GameMainScreenView.Listener>() {

    interface Listener {
        fun onExitButtonClicked()

        fun onPauseButtonClicked()

        fun onCheckAnswerButtonClicked(selectedRomanization: String)

        fun onPlayClickSoundEffect()
    }

    abstract fun bindDifficultyValue(difficultyValue: Int)

    abstract fun updateScore(score: Int)

    abstract fun updateHiraganaSymbol(symbol: String)

    abstract fun updateCountdownTimer(countdownTime: Int)

    abstract fun updateRomanizationOptions(romanizationOptions: List<String>)

    abstract fun updateProgress(progress: Int)

    abstract fun clearCheckedRomanization()
}