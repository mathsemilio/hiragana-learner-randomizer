package com.mathsemilio.hiraganalearner.ui.screens.game.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.mathsemilio.hiraganalearner.data.preferences.repository.PreferencesRepository
import com.mathsemilio.hiraganalearner.others.SoundEffectsModule
import com.mathsemilio.hiraganalearner.ui.others.ScreensNavigator
import com.mathsemilio.hiraganalearner.ui.screens.common.BaseFragment
import com.mathsemilio.hiraganalearner.ui.screens.common.InterstitialAdUseCase

class GameWelcomeScreen : BaseFragment(), GameWelcomeScreenView.Listener, InterstitialAdUseCase.Listener {

    private lateinit var gameWelcomeScreenView: GameWelcomeScreenViewImpl

    private lateinit var preferencesRepository: PreferencesRepository
    private lateinit var soundEffectsModule: SoundEffectsModule
    private lateinit var screensNavigator: ScreensNavigator

    private lateinit var interstitialAdUseCase: InterstitialAdUseCase
    private lateinit var adRequest: AdRequest

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        gameWelcomeScreenView = compositionRoot.viewFactory.getGameWelcomeScreenView(container)
        return gameWelcomeScreenView.getRootView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialize()

        interstitialAdUseCase.addListener(this)

        gameWelcomeScreenView.setupUI(preferencesRepository.gameDefaultOption)
    }

    private fun initialize() {
        preferencesRepository = compositionRoot.preferencesRepository

        soundEffectsModule = compositionRoot.soundEffectsModule

        screensNavigator = compositionRoot.screensNavigator

        adRequest = compositionRoot.adRequest

        interstitialAdUseCase = compositionRoot.interstitialAdUseCase
    }

    override fun onPlayClickSoundEffect() {
        soundEffectsModule.playClickSoundEffect()
    }

    override fun onSettingsIconClicked() {
        screensNavigator.navigateToSettingsScreen()
    }

    override fun onStartButtonClicked(difficultyValue: Int) {
        soundEffectsModule.playButtonClickSoundEffect()
        interstitialAdUseCase.showInterstitialAd()
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
        interstitialAdUseCase.removeListener(this)
        super.onDestroyView()
    }
}