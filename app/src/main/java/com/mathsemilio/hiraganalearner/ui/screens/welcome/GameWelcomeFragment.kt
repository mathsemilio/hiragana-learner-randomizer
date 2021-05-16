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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.*
import com.mathsemilio.hiraganalearner.data.manager.PreferencesManager
import com.mathsemilio.hiraganalearner.others.SoundEffectsModule
import com.mathsemilio.hiraganalearner.ui.common.BaseFragment
import com.mathsemilio.hiraganalearner.ui.common.helper.InterstitialAdHelper
import com.mathsemilio.hiraganalearner.ui.common.navigation.ScreensNavigator

class GameWelcomeFragment : BaseFragment(),
    GameWelcomeScreenView.Listener,
    InterstitialAdHelper.Listener {

    private lateinit var view: GameWelcomeScreenView

    private lateinit var preferencesManager: PreferencesManager
    private lateinit var soundEffectsModule: SoundEffectsModule
    private lateinit var screensNavigator: ScreensNavigator

    private lateinit var interstitialAdHelper: InterstitialAdHelper

    private var difficultyValue = 0
    private lateinit var defaultDifficultyValue: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screensNavigator = compositionRoot.screensNavigator
        interstitialAdHelper = compositionRoot.interstitialAdHelper
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = compositionRoot.viewFactory.getGameWelcomeScreenView(container)
        return view.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferencesManager = compositionRoot.preferencesManager
        soundEffectsModule = compositionRoot.soundEffectsModule

        defaultDifficultyValue = preferencesManager.defaultDifficultyValue

        setDifficultyValueBasedOnDefaultValue()
    }

    private fun setDifficultyValueBasedOnDefaultValue() {
        when (defaultDifficultyValue) {
            "0" -> SHOW_DIFFICULTY_OPTIONS
            "1" -> DEFAULT_DIFFICULTY_BEGINNER
            "2" -> DEFAULT_DIFFICULTY_MEDIUM
            "3" -> DEFAULT_DIFFICULTY_HARD
            else -> throw IllegalArgumentException(ILLEGAL_DEFAULT_DIFFICULTY_VALUE)
        }.also { defaultDifficulty ->
            when (defaultDifficulty) {
                DEFAULT_DIFFICULTY_BEGINNER -> difficultyValue = GAME_DIFFICULTY_VALUE_BEGINNER
                DEFAULT_DIFFICULTY_MEDIUM -> difficultyValue = GAME_DIFFICULTY_VALUE_MEDIUM
                DEFAULT_DIFFICULTY_HARD -> difficultyValue = GAME_DIFFICULTY_VALUE_HARD
            }
        }

        view.bindDifficultyValues(defaultDifficultyValue, difficultyValue)
    }

    override fun onSettingsIconClicked() {
        screensNavigator.toSettingsScreen()
    }

    override fun onDifficultyOptionSelected(difficulty: String) {
        difficultyValue = when (difficulty) {
            getString(R.string.game_difficulty_beginner) -> GAME_DIFFICULTY_VALUE_BEGINNER
            getString(R.string.game_difficulty_medium) -> GAME_DIFFICULTY_VALUE_MEDIUM
            getString(R.string.game_difficulty_hard) -> GAME_DIFFICULTY_VALUE_HARD
            else -> throw IllegalArgumentException(ILLEGAL_GAME_DIFFICULTY_VALUE)
        }
    }

    override fun onPlayClickSoundEffect() {
        soundEffectsModule.playClickSoundEffect()
    }

    override fun onStartButtonClicked() {
        soundEffectsModule.playButtonClickSoundEffect()
        interstitialAdHelper.showInterstitialAd()
    }

    override fun onAdDismissed() {
        screensNavigator.toMainScreen(difficultyValue)
    }

    override fun onShowAdFailed() {
        screensNavigator.toMainScreen(difficultyValue)
    }

    override fun onStart() {
        view.addListener(this)
        interstitialAdHelper.addListener(this)
        super.onStart()
    }

    override fun onStop() {
        view.removeListener(this)
        interstitialAdHelper.removeListener(this)
        super.onStop()
    }
}