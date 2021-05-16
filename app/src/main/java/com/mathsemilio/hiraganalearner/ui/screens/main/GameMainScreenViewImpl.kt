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

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.chip.Chip
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.*
import com.mathsemilio.hiraganalearner.databinding.GameMainScreenBinding

class GameMainScreenViewImpl(inflater: LayoutInflater, container: ViewGroup?) :
    GameMainScreenView() {

    private lateinit var currentSelectedRomanization: String

    init {
        binding = GameMainScreenBinding.inflate(inflater, container, false).apply {
            fabExit.setOnClickListener { notifyExitButtonClick() }
            fabPause.setOnClickListener { notifyPauseButtonClick() }
            fabCheckAnswer.setOnClickListener { notifyCheckAnswerButtonClick() }
        }
        setRomanizationOptionsChipGroupOnCheckedChangedListener()
    }

    private fun setRomanizationOptionsChipGroupOnCheckedChangedListener() {
        binding.apply {
            chipGroupRomanizationOptions.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId == -1) {
                    fabCheckAnswer.isEnabled = false
                } else {
                    fabCheckAnswer.isEnabled = true

                    notifyPlayClickSoundEffect()

                    val checkedChip = group.findViewById<Chip>(checkedId)
                    currentSelectedRomanization = checkedChip.text.toString()
                }
            }
        }
    }

    override fun bindDifficultyValue(difficultyValue: Int) {
        setupCurrentDifficultyTextView(difficultyValue)
        setupCountdownTimer(difficultyValue)
    }

    private fun setupCurrentDifficultyTextView(difficultyValue: Int) {
        binding.textViewGameDifficulty.text = when (difficultyValue) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> getString(R.string.game_difficulty_beginner)
            GAME_DIFFICULTY_VALUE_MEDIUM -> getString(R.string.game_difficulty_medium)
            GAME_DIFFICULTY_VALUE_HARD -> getString(R.string.game_difficulty_hard)
            else -> throw IllegalArgumentException(ILLEGAL_GAME_DIFFICULTY_VALUE)
        }
    }

    private fun setupCountdownTimer(difficultyValue: Int) {
        binding.gameTimerProgressBar.max = when (difficultyValue) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> PROGRESS_BAR_MAX_VALUE_BEGINNER
            GAME_DIFFICULTY_VALUE_MEDIUM -> PROGRESS_BAR_MAX_VALUE_MEDIUM
            GAME_DIFFICULTY_VALUE_HARD -> PROGRESS_BAR_MAX_VALUE_HARD
            else -> throw IllegalArgumentException(ILLEGAL_GAME_DIFFICULTY_VALUE)
        }
    }

    override fun updateScore(score: Int) {
        binding.textViewCurrentScore.text = context.getString(R.string.game_score, score)
    }

    override fun updateHiraganaSymbol(symbol: String) {
        binding.textViewCurrentHiraganaSymbol.text = symbol
    }

    override fun updateCountdownTimer(countdownTime: Int) {
        binding.gameTimerProgressBar.progress = countdownTime
    }

    override fun updateRomanizationOptions(romanizationOptions: List<String>) {
        binding.apply {
            chipButtonOption1.text = romanizationOptions[0]
            chipButtonOption2.text = romanizationOptions[1]
            chipButtonOption3.text = romanizationOptions[2]
            chipButtonOption4.text = romanizationOptions[3]
        }
    }

    override fun updateProgress(progress: Int) {
        binding.progressBarGameProgress.progress = progress
    }

    override fun clearCheckedRomanization() {
        binding.chipGroupRomanizationOptions.clearCheck()
    }

    private fun notifyExitButtonClick() {
        listeners.forEach { listener ->
            listener.onExitButtonClicked()
        }
    }

    private fun notifyPauseButtonClick() {
        listeners.forEach { listener ->
            listener.onPauseButtonClicked()
        }
    }

    private fun notifyCheckAnswerButtonClick() {
        listeners.forEach { listener ->
            listener.onCheckAnswerButtonClicked(currentSelectedRomanization)
        }
    }

    private fun notifyPlayClickSoundEffect() {
        listeners.forEach { listener ->
            listener.onPlayClickSoundEffect()
        }
    }
}