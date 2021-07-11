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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mathsemilio.hiraganalearner.common.*
import com.mathsemilio.hiraganalearner.common.eventbus.EventListener
import com.mathsemilio.hiraganalearner.common.eventbus.EventSubscriber
import com.mathsemilio.hiraganalearner.common.provider.BackPressedCallbackProvider
import com.mathsemilio.hiraganalearner.data.manager.PreferencesManager
import com.mathsemilio.hiraganalearner.domain.backend.BackendMediator
import com.mathsemilio.hiraganalearner.domain.model.SyllabarySymbol
import com.mathsemilio.hiraganalearner.others.SoundEffectsModule
import com.mathsemilio.hiraganalearner.ui.common.BaseFragment
import com.mathsemilio.hiraganalearner.ui.common.event.PromptDialogEvent
import com.mathsemilio.hiraganalearner.ui.common.helper.InterstitialAdHelper
import com.mathsemilio.hiraganalearner.ui.common.manager.DialogManager
import com.mathsemilio.hiraganalearner.ui.common.navigation.ScreensNavigator

class GameMainFragment : BaseFragment(),
    GameMainScreenView.Listener,
    BackendMediator.Listener,
    InterstitialAdHelper.Listener,
    EventListener {

    companion object {
        @JvmStatic
        fun withDifficultyValue(difficultyValue: Int): GameMainFragment {
            val args = Bundle(1).apply { putInt(ARG_DIFFICULTY_VALUE, difficultyValue) }
            val gameMainFragment = GameMainFragment()
            gameMainFragment.arguments = args
            return gameMainFragment
        }
    }

    enum class ScreenState { TIMER_RUNNING, TIMER_PAUSED, DIALOG_BEING_SHOWN }

    private lateinit var view: GameMainScreenView

    private lateinit var backendMediator: BackendMediator

    private lateinit var backPressedCallbackProvider: BackPressedCallbackProvider
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var soundEffectsModule: SoundEffectsModule
    private lateinit var screensNavigator: ScreensNavigator
    private lateinit var dialogManager: DialogManager

    private lateinit var eventSubscriber: EventSubscriber

    private lateinit var interstitialAdHelper: InterstitialAdHelper

    private var difficultyValue = 0
    private var currentScreenState = ScreenState.TIMER_RUNNING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backendMediator = compositionRoot.backendMediator
        backPressedCallbackProvider = compositionRoot.backPressedCallbackProvider
        preferencesManager = compositionRoot.preferencesManager
        soundEffectsModule = compositionRoot.soundEffectsModule
        screensNavigator = compositionRoot.screensNavigator
        dialogManager = compositionRoot.dialogManager
        eventSubscriber = compositionRoot.eventSubscriber
        interstitialAdHelper = compositionRoot.interstitialAdHelper
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = GameMainScreenViewImpl(inflater, container)
        return view.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        difficultyValue = requireArguments().getInt(ARG_DIFFICULTY_VALUE, 0)

        backendMediator.addListener(this)
        backendMediator.startGame(difficultyValue)

        bindDifficultyValue()

        setupOnBackPressedDispatcher()
    }

    private fun bindDifficultyValue() {
        view.bindDifficultyValue(difficultyValue)
    }

    private fun setupOnBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallbackProvider.getOnBackPressedCallback { onExitButtonClicked() }
        )
    }

    private fun getNextSymbol() {
        if (backendMediator.gameFinished) {
            if (backendMediator.currentScore == PERFECT_SCORE)
                preferencesManager.incrementPerfectScoresAchieved()

            interstitialAdHelper.showInterstitialAd()
        } else {
            backendMediator.getNextSymbol()
        }
    }

    override fun onExitButtonClicked() {
        backendMediator.pauseTimer()
        soundEffectsModule.playButtonClickSoundEffect()
        currentScreenState = ScreenState.DIALOG_BEING_SHOWN
        dialogManager.showExitGameDialog()
    }

    override fun onPauseButtonClicked() {
        backendMediator.pauseTimer()
        soundEffectsModule.playButtonClickSoundEffect()
        currentScreenState = ScreenState.DIALOG_BEING_SHOWN
        dialogManager.showGamePausedDialog()
    }

    override fun onCheckAnswerButtonClicked(selectedRomanization: String) {
        soundEffectsModule.playButtonClickSoundEffect()
        currentScreenState = ScreenState.DIALOG_BEING_SHOWN
        backendMediator.checkAnswer(selectedRomanization)
    }

    override fun onPlayClickSoundEffect() {
        soundEffectsModule.playClickSoundEffect()
    }

    override fun onScoreUpdated(score: Int) {
        view.updateScore(score)
    }

    override fun onProgressUpdated(progress: Int) {
        view.updateProgress(progress)
    }

    override fun onCountdownTimeUpdated(countdownTime: Int) {
        view.updateCountdownTimer(countdownTime)
    }

    override fun onRomanizationOptionsUpdated(romanizationOptions: List<String>) {
        view.updateRomanizationOptions(romanizationOptions)
    }

    override fun onSymbolUpdated(symbol: SyllabarySymbol) {
        view.updateHiraganaSymbol(symbol.symbol)
    }

    override fun onCorrectAnswer() {
        soundEffectsModule.playSuccessSoundEffect()
        currentScreenState = ScreenState.DIALOG_BEING_SHOWN
        dialogManager.showCorrectAnswerDialog()
    }

    override fun onWrongAnswer() {
        soundEffectsModule.playErrorSoundEffect()
        currentScreenState = ScreenState.DIALOG_BEING_SHOWN
        dialogManager.showWrongAnswerDialog(backendMediator.currentSymbol.romanization)
    }

    override fun onGameTimeOver() {
        soundEffectsModule.playErrorSoundEffect()
        currentScreenState = ScreenState.DIALOG_BEING_SHOWN
        dialogManager.showTimeOverDialog(backendMediator.currentSymbol.romanization)
    }

    override fun onAdDismissed() {
        screensNavigator.toResultScreen(difficultyValue, backendMediator.currentScore)
    }

    override fun onShowAdFailed() {
        screensNavigator.toResultScreen(difficultyValue, backendMediator.currentScore)
    }

    override fun onEvent(event: Any) {
        when (event) {
            is PromptDialogEvent -> handlePromptDialogEvent(event)
        }
    }

    private fun handlePromptDialogEvent(event: PromptDialogEvent) {
        when (event) {
            PromptDialogEvent.PositiveButtonClicked ->
                handlePromptDialogPositiveButtonClickBasedOnTag(dialogManager.currentDialogTag)
            PromptDialogEvent.NegativeButtonClicked ->
                handlePromptDialogNegativeButtonClickBasedOnTag(dialogManager.currentDialogTag)
        }
    }

    private fun handlePromptDialogPositiveButtonClickBasedOnTag(tag: String) {
        when (tag) {
            TAG_CORRECT_ANSWER_DIALOG -> {
                getNextSymbol()
                view.clearCheckedRomanization()
                currentScreenState = ScreenState.TIMER_RUNNING
            }
            TAG_WRONG_ANSWER_DIALOG -> {
                getNextSymbol()
                view.clearCheckedRomanization()
                currentScreenState = ScreenState.TIMER_RUNNING
            }
            TAG_TIME_OVER_DIALOG -> {
                getNextSymbol()
                view.clearCheckedRomanization()
                currentScreenState = ScreenState.TIMER_RUNNING
            }
            TAG_EXIT_GAME_DIALOG -> {
                backendMediator.resumeTimer()
                currentScreenState = ScreenState.TIMER_RUNNING
            }
            TAG_GAME_PAUSED_DIALOG -> {
                backendMediator.resumeTimer()
                currentScreenState = ScreenState.TIMER_RUNNING
            }
        }
    }

    private fun handlePromptDialogNegativeButtonClickBasedOnTag(tag: String) {
        when (tag) {
            TAG_EXIT_GAME_DIALOG -> screensNavigator.toWelcomeScreen()
        }
    }

    override fun onStart() {
        view.addListener(this)
        eventSubscriber.subscribe(this)
        interstitialAdHelper.addListener(this)
        super.onStart()
    }

    override fun onResume() {
        super.onResume()

        if (currentScreenState == ScreenState.TIMER_PAUSED) {
            backendMediator.resumeTimer()
            currentScreenState = ScreenState.TIMER_RUNNING
        }
    }

    override fun onPause() {
        super.onPause()
        backendMediator.pauseTimer()

        if (currentScreenState == ScreenState.TIMER_RUNNING)
            currentScreenState = ScreenState.TIMER_PAUSED
    }

    override fun onStop() {
        view.removeListener(this)
        eventSubscriber.unsubscribe(this)
        interstitialAdHelper.removeListener(this)
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        backendMediator.removeListener(this)
    }
}