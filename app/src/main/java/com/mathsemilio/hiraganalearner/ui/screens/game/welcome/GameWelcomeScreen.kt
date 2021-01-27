package com.mathsemilio.hiraganalearner.ui.screens.game.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.data.preferences.repository.PreferencesRepository
import com.mathsemilio.hiraganalearner.others.soundeffects.SoundEffectsModule
import com.mathsemilio.hiraganalearner.ui.others.ScreensNavigator
import com.mathsemilio.hiraganalearner.ui.screens.common.BaseFragment

class GameWelcomeScreen : BaseFragment(), GameWelcomeScreenView.Listener {

    private lateinit var mView: GameWelcomeScreenViewImpl

    private lateinit var mPreferencesRepository: PreferencesRepository
    private lateinit var mSoundEffectsModule: SoundEffectsModule
    private lateinit var mScreensNavigator: ScreensNavigator

    private var mInterstitialAd: InterstitialAd? = null
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

        initializeInterstitialAd()
    }

    private fun initialize() {
        mPreferencesRepository = getCompositionRoot().getPreferencesRepository()

        mSoundEffectsModule = getCompositionRoot().getSoundEffectsModule(
            mPreferencesRepository.getSoundEffectsVolume()
        )

        mScreensNavigator = getCompositionRoot().getScreensNavigator()

        mAdRequest = getCompositionRoot().getAdRequest()
    }

    private fun initializeInterstitialAd() {
        InterstitialAd.load(
            requireContext(),
            getString(R.string.interstitialAdTestUnitId),
            mAdRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    mInterstitialAd?.fullScreenContentCallback = getFullScreenContentCallback()
                }
            }
        )
    }

    private fun getFullScreenContentCallback(): FullScreenContentCallback {
        return object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                mScreensNavigator.navigateToMainScreen(mView.getDifficultyValue())
            }

            override fun onAdDismissedFullScreenContent() {
                mScreensNavigator.navigateToMainScreen(mView.getDifficultyValue())
            }
        }
    }

    override fun onPlayClickSoundEffect() {
        mSoundEffectsModule.playClickSoundEffect()
    }

    override fun onSettingsIconClicked() {
        mScreensNavigator.navigateToSettingsScreen()
    }

    override fun onStartButtonClicked(difficultyValue: Int) {
        mSoundEffectsModule.playButtonClickSoundEffect()
        if (mInterstitialAd == null)
            mScreensNavigator.navigateToMainScreen(difficultyValue)
        else
            mInterstitialAd?.show(requireActivity())
    }

    override fun onStart() {
        mView.registerListener(this)
        super.onStart()
    }

    override fun onDestroyView() {
        mView.removeListener(this)
        mInterstitialAd = null
        super.onDestroyView()
    }
}