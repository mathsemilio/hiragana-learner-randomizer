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

package com.mathsemilio.hiraganalearner.ui.screens.welcome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.chip.Chip
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.*
import com.mathsemilio.hiraganalearner.databinding.GameWelcomeScreenBinding

class GameWelcomeScreenViewImpl(
    inflater: LayoutInflater,
    container: ViewGroup?
) : GameWelcomeScreenView() {

    private var difficultyValue = 0

    init {
        binding = GameWelcomeScreenBinding.inflate(inflater, container, false).apply {
            imageViewSettingsIcon.setOnClickListener { notifySettingsIconClick() }
            buttonStart.setOnClickListener { notifyStartButtonClick() }
        }
    }

    override fun bindDifficultyValues(defaultDifficultyValue: String, difficultyValue: Int) {
        this.difficultyValue = difficultyValue

        when {
            defaultDifficultyValue == SHOW_DIFFICULTY_OPTIONS ->
                setDifficultyOptionsChipGroupOnCheckedChangedListener()
            defaultDifficultyValue != "0" ->
                setupUIForDifficultyPreSelected()
        }
    }

    private fun setDifficultyOptionsChipGroupOnCheckedChangedListener() {
        binding.apply {
            chipGroupGameDifficulty.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId == -1) {
                    buttonStart.isEnabled = false
                } else {
                    buttonStart.isEnabled = true

                    notifyPlayClickSoundEffect()

                    val checkedChip = group.findViewById<Chip>(checkedId)
                    notifyDifficultyOptionSelected(checkedChip.text.toString())
                }
            }
        }
    }

    private fun setupUIForDifficultyPreSelected() {
        showOnDifficultyTextView()

        binding.apply {
            textViewSelectADifficulty.isVisible = false
            chipGroupGameDifficulty.isVisible = false
            buttonStart.isEnabled = true
        }
    }

    private fun showOnDifficultyTextView() {
        binding.textViewOnGameDifficulty.apply {
            isVisible = true
            text = context.getString(
                R.string.on_game_difficulty, when (difficultyValue) {
                    GAME_DIFFICULTY_VALUE_BEGINNER -> context.getString(R.string.game_difficulty_beginner)
                    GAME_DIFFICULTY_VALUE_MEDIUM -> context.getString(R.string.game_difficulty_medium)
                    GAME_DIFFICULTY_VALUE_HARD -> context.getString(R.string.game_difficulty_hard)
                    else -> throw IllegalArgumentException(ILLEGAL_GAME_DIFFICULTY_VALUE)
                }
            )
        }
    }

    private fun notifySettingsIconClick() {
        notifyListener { listener ->
            listener.onSettingsIconClicked()
        }
    }

    private fun notifyStartButtonClick() {
        notifyListener { listener ->
            listener.onStartButtonClicked()
        }
    }

    private fun notifyPlayClickSoundEffect() {
        notifyListener { listener ->
            listener.onPlayClickSoundEffect()
        }
    }

    private fun notifyDifficultyOptionSelected(difficulty: String) {
        notifyListener { listener ->
            listener.onDifficultyOptionSelected(difficulty)
        }
    }
}