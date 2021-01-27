package com.mathsemilio.hiraganalearner.ui.screens.game.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.mathsemilio.hiraganalearner.data.preferences.repository.PreferencesRepository
import com.mathsemilio.hiraganalearner.others.soundeffects.SoundEffectsModule
import com.mathsemilio.hiraganalearner.ui.others.ScreensNavigator
import com.mathsemilio.hiraganalearner.ui.screens.common.BaseFragment
import com.mathsemilio.hiraganalearner.ui.usecase.OnInterstitialAdEventListener
import com.mathsemilio.hiraganalearner.ui.usecase.ShowInterstitialAdUseCase

class GameWelcomeScreen : BaseFragment(), GameWelcomeScreenView.Listener,
    OnInterstitialAdEventListener {

    private lateinit var mView: GameWelcomeScreenViewImpl

    private lateinit var mPreferencesRepository: PreferencesRepository
    private lateinit var mSoundEffectsModule: SoundEffectsModule
    private lateinit var mScreensNavigator: ScreensNavigator

    private lateinit var mShowInterstitialAdUseCase: ShowInterstitialAdUseCase
    private lateinit var mAdRequest: AdRequest

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView = getCompositionRoot().getViewFactory().getGameWelcomeScreenView(container)
        return mView.getRootView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialize()

        mView.onControllerViewCreated(mPreferencesRepository.getGameDefaultOption())

        mShowInterstitialAdUseCase.registerListener(this)
    }

    private fun initialize() {
        mPreferencesRepository = getCompositionRoot().getPreferencesRepository()

        mSoundEffectsModule = getCompositionRoot().getSoundEffectsModule(
            mPreferencesRepository.getSoundEffectsVolume()
        )

        mScreensNavigator = getCompositionRoot().getScreensNavigator()

        mAdRequest = getCompositionRoot().getAdRequest()

        mShowInterstitialAdUseCase = getCompositionRoot().getShowInterstitialAdUseCase()
    }

    override fun onPlayClickSoundEffect() {
        mSoundEffectsModule.playClickSoundEffect()
    }

    override fun onSettingsIconClicked() {
        mScreensNavigator.navigateToSettingsScreen()
    }

    override fun onStartButtonClicked(difficultyValue: Int) {
        mSoundEffectsModule.playButtonClickSoundEffect()
        mShowInterstitialAdUseCase.showInterstitialAd()
    }

    override fun onShowInterstitialAdFailed() {
        mScreensNavigator.navigateToMainScreen(mView.getDifficultyValue())
    }

    override fun onInterstitialAdDismissed() {
        mScreensNavigator.navigateToMainScreen(mView.getDifficultyValue())
    }

    override fun onStart() {
        mView.registerListener(this)
        super.onStart()
    }

    override fun onDestroyView() {
        mView.removeListener(this)
        mShowInterstitialAdUseCase.removeListener(this)
        super.onDestroyView()
    }
}