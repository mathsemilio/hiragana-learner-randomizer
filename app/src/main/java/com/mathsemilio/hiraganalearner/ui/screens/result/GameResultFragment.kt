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

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.ARG_DIFFICULTY_VALUE
import com.mathsemilio.hiraganalearner.common.ARG_SCORE
import com.mathsemilio.hiraganalearner.common.PERFECT_SCORE
import com.mathsemilio.hiraganalearner.common.provider.BackPressedCallbackProvider
import com.mathsemilio.hiraganalearner.data.manager.PreferencesManager
import com.mathsemilio.hiraganalearner.others.SoundEffectsModule
import com.mathsemilio.hiraganalearner.ui.common.BaseFragment
import com.mathsemilio.hiraganalearner.ui.common.navigation.ScreensNavigator

class GameResultFragment private constructor() : BaseFragment(), GameResultScreenView.Listener {

    companion object Factory {
        fun withGameResult(difficultyValue: Int, score: Int): GameResultFragment {
            val args = Bundle(2).apply {
                putInt(ARG_DIFFICULTY_VALUE, difficultyValue)
                putInt(ARG_SCORE, score)
            }
            val gameResultFragment = GameResultFragment()
            gameResultFragment.arguments = args
            return gameResultFragment
        }
    }

    private lateinit var view: GameResultScreenView

    private lateinit var backPressedCallbackProvider: BackPressedCallbackProvider
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var soundEffectsModule: SoundEffectsModule
    private lateinit var screensNavigator: ScreensNavigator

    private lateinit var adRequest: AdRequest

    private var score = 0
    private var difficultyValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backPressedCallbackProvider = compositionRoot.backPressedCallbackProvider
        preferencesManager = compositionRoot.preferencesManager
        soundEffectsModule = compositionRoot.soundEffectsModule
        screensNavigator = compositionRoot.screensNavigator
        adRequest = compositionRoot.adRequest
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = compositionRoot.viewFactory.getGameResultScreenView(container)
        return view.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOnBackPressedDispatcher()

        score = requireArguments().getInt(ARG_SCORE, 0)

        difficultyValue = requireArguments().getInt(ARG_DIFFICULTY_VALUE, 0)
    }

    private fun setupOnBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallbackProvider.getOnBackPressedCallback { screensNavigator.toWelcomeScreen() }
        )
    }

    override fun onHomeButtonClicked() {
        soundEffectsModule.playButtonClickSoundEffect()
        screensNavigator.toWelcomeScreen()
    }

    override fun onPlayAgainButtonClicked() {
        soundEffectsModule.playButtonClickSoundEffect()
        screensNavigator.toMainScreen(difficultyValue)
    }

    override fun onShareScoreButtonClicked() {
        soundEffectsModule.playButtonClickSoundEffect()
        shareScore(score)
    }

    private fun getShareScoreIntent(score: Int): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getShareScoreMessage(score))
            type = "text/plain"
        }
    }

    private fun getShareScoreMessage(score: Int): String {
        return if (score == PERFECT_SCORE)
            getString(R.string.share_perfect_final_score)
        else
            getString(R.string.share_final_score, score)
    }

    private fun shareScore(score: Int) {
        startActivity(
            Intent.createChooser(
                getShareScoreIntent(score),
                getString(R.string.game_score_create_chooser_title)
            )
        )
    }

    override fun onStart() {
        super.onStart()
        view.addListener(this)
        view.bindGameResult(score, difficultyValue, preferencesManager.perfectScoresAchieved)
    }

    override fun onStop() {
        super.onStop()
        view.removeListener(this)
    }
}