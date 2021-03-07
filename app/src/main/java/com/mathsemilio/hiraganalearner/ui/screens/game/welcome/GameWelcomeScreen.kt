package com.mathsemilio.hiraganalearner.ui.screens.game.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mathsemilio.hiraganalearner.data.repository.PreferencesRepository
import com.mathsemilio.hiraganalearner.others.SoundEffectsModule
import com.mathsemilio.hiraganalearner.ui.common.BaseFragment
import com.mathsemilio.hiraganalearner.ui.common.helper.InterstitialAdUseHelper
import com.mathsemilio.hiraganalearner.ui.common.helper.ScreensNavigator

class GameWelcomeScreen : BaseFragment(),
    GameWelcomeScreenView.Listener,
    InterstitialAdUseHelper.Listener {

    private lateinit var gameWelcomeScreenView: GameWelcomeScreenViewImpl

    private lateinit var preferencesRepository: PreferencesRepository
    private lateinit var soundEffectsModule: SoundEffectsModule
    private lateinit var screensNavigator: ScreensNavigator

    private lateinit var interstitialAdUseHelper: InterstitialAdUseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        gameWelcomeScreenView = compositionRoot.viewFactory.getGameWelcomeScreenView(container)
        return gameWelcomeScreenView.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialize()

        interstitialAdUseHelper.addListener(this)

        gameWelcomeScreenView.setupUI(preferencesRepository.gameDefaultOption)
    }

    private fun initialize() {
        preferencesRepository = compositionRoot.preferencesRepository

        soundEffectsModule = compositionRoot.soundEffectsModule

        screensNavigator = compositionRoot.screensNavigator

        interstitialAdUseHelper = compositionRoot.interstitialAdHelper
    }

    override fun onPlayClickSoundEffect() {
        soundEffectsModule.playClickSoundEffect()
    }

    override fun onSettingsIconClicked() {
        screensNavigator.navigateToSettingsScreen()
    }

    override fun onStartButtonClicked(difficultyValue: Int) {
        soundEffectsModule.playButtonClickSoundEffect()
        interstitialAdUseHelper.showInterstitialAd()
    }

    override fun onAdDismissed() {
        screensNavigator.navigateToMainScreen(gameWelcomeScreenView.getDifficultyValue())
    }

    override fun onAdFailedToShow() {
        screensNavigator.navigateToMainScreen(gameWelcomeScreenView.getDifficultyValue())
    }

    override fun onStart() {
        gameWelcomeScreenView.addListener(this)
        super.onStart()
    }

    override fun onStop() {
        gameWelcomeScreenView.removeListener(this)
        super.onStop()
    }

    override fun onDestroyView() {
        interstitialAdUseHelper.removeListener(this)
        super.onDestroyView()
    }
}