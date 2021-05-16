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
package com.mathsemilio.hiraganalearner.ui.screens.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.gms.ads.AdRequest
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.*
import com.mathsemilio.hiraganalearner.databinding.GameResultScreenBinding

class GameResultScreenViewImpl(inflater: LayoutInflater, container: ViewGroup?) : GameResultScreenView() {

    private var currentDifficultyValue = 0

    init {
        binding = GameResultScreenBinding.inflate(inflater, container, false).apply {
            fabHome.setOnClickListener { notifyHomeButtonClick() }
            fabPlayAgain.setOnClickListener { notifyPlayAgainButtonClick() }
            fabShare.setOnClickListener { notifyShareScoreButtonClick() }
        }
    }

    override fun bindGameResult(score: Int, difficultyValue: Int, perfectScores: Int) {
        currentDifficultyValue = difficultyValue

        if (score == 0) binding.fabShare.isVisible = false

        setupYouGotSymbolsCorrectlyTextView(score)
        setupGameDifficultyTextView(difficultyValue)
        setupPerfectScoresAchievedTextView(perfectScores)
    }

    private fun setupYouGotSymbolsCorrectlyTextView(score: Int) {
        binding.textViewYouGotCorrectly.text = when (score) {
            1 -> context.getString(R.string.you_got_one_symbol_correctly, score)
            PERFECT_SCORE -> context.getString(R.string.you_got_all_symbols_correctly)
            else -> context.getString(R.string.you_got_symbols_correctly, score)
        }
    }

    private fun setupGameDifficultyTextView(difficultyValue: Int) {
        binding.textViewGameDifficultyValue.text = when (difficultyValue) {
            GAME_DIFFICULTY_VALUE_BEGINNER -> getString(R.string.game_difficulty_beginner)
            GAME_DIFFICULTY_VALUE_MEDIUM -> getString(R.string.game_difficulty_medium)
            GAME_DIFFICULTY_VALUE_HARD -> getString(R.string.game_difficulty_hard)
            else -> throw IllegalArgumentException(ILLEGAL_GAME_DIFFICULTY_VALUE)
        }
    }

    private fun setupPerfectScoresAchievedTextView(perfectScores: Int) {
        binding.textViewPerfectScoresAchieved.text = perfectScores.toString()
    }

    override fun loadAdBanner(adRequest: AdRequest) {
        binding.adviewGameResultScreen.loadAd(adRequest)
    }

    private fun notifyHomeButtonClick() {
        listeners.forEach { listener ->
            listener.onHomeButtonClicked()
        }
    }

    private fun notifyPlayAgainButtonClick() {
        listeners.forEach { listener ->
            listener.onPlayAgainButtonClicked()
        }
    }

    private fun notifyShareScoreButtonClick() {
        listeners.forEach { listener ->
            listener.onShareScoreButtonClicked()
        }
    }
}