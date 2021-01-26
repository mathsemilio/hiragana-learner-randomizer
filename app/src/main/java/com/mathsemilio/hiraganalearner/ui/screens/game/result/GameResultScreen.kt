package com.mathsemilio.hiraganalearner.ui.screens.game.result

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
import com.mathsemilio.hiraganalearner.common.ARG_DIFFICULTY_VALUE
import com.mathsemilio.hiraganalearner.common.ARG_SCORE
import com.mathsemilio.hiraganalearner.data.preferences.repository.PreferencesRepository
import com.mathsemilio.hiraganalearner.others.soundeffects.SoundEffectsModule
import com.mathsemilio.hiraganalearner.ui.others.ScreensNavigator
import com.mathsemilio.hiraganalearner.ui.screens.common.BaseFragment
import com.mathsemilio.hiraganalearner.ui.screens.game.result.usecase.ShareGameScoreUseCase

class GameResultScreen : BaseFragment(), GameResultScreenView.Listener {

    companion object {
        fun newInstance(difficultyValue: Int, score: Int): GameResultScreen {
            val args = Bundle().apply {
                putInt(ARG_DIFFICULTY_VALUE, difficultyValue)
                putInt(ARG_SCORE, score)
            }
            val gameResultScreenFragment = GameResultScreen()
            gameResultScreenFragment.arguments = args
            return gameResultScreenFragment
        }
    }

    private enum class UserAction { GO_TO_WELCOME_SCREEN, GO_TO_MAIN_SCREEN }

    private lateinit var mView: GameResultScreenViewImpl

    private lateinit var mShareGameScoreUseCase: ShareGameScoreUseCase
    private lateinit var mPreferencesRepository: PreferencesRepository
    private lateinit var mSoundEffectsModule: SoundEffectsModule
    private lateinit var mScreensNavigator: ScreensNavigator

    private var mInterstitialAd: InterstitialAd? = null
    private lateinit var mAdRequest: AdRequest

    private lateinit var intendedUserAction: UserAction

    private var mScore = 0
    private var mDifficultyValue = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView = getCompositionRoot().getViewFactory().getGameResultScreenView(container)
        return mView.getRootView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialize()

        mView.onControllerViewCreated(
            mDifficultyValue,
            mScore,
            mPreferencesRepository.getPerfectScoresValue()
        )

        mView.loadGameResultScreenBannerAd(mAdRequest)

        initializeInterstitialAd()
    }

    private fun initialize() {
        mDifficultyValue = getDifficultyValue()
        mScore = getScore()

        mShareGameScoreUseCase = getCompositionRoot().getShareGameScoreUseCase(mScore)

        mPreferencesRepository = getCompositionRoot().getPreferencesRepository()

        mSoundEffectsModule = getCompositionRoot().getSoundEffectsModule(
            mPreferencesRepository.getSoundEffectsVolume()
        )

        mScreensNavigator = getCompositionRoot().getScreensNavigator()

        mAdRequest = getCompositionRoot().getAdRequest()

        getCompositionRoot().getBackPressedDispatcher { onHomeButtonClicked() }
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
            override fun onAdFailedToShowFullScreenContent(adError: AdError?) = handleNavigation()
            override fun onAdDismissedFullScreenContent() = handleNavigation()
        }
    }

    private fun handleNavigation() {
        when (intendedUserAction) {
            UserAction.GO_TO_WELCOME_SCREEN -> mScreensNavigator.navigateToWelcomeScreen()
            UserAction.GO_TO_MAIN_SCREEN -> mScreensNavigator.navigateToMainScreen(mDifficultyValue)
        }
    }

    private fun getDifficultyValue(): Int {
        return arguments?.getInt(ARG_DIFFICULTY_VALUE) ?: 0
    }

    private fun getScore(): Int {
        return arguments?.getInt(ARG_SCORE) ?: 0
    }

    override fun onHomeButtonClicked() {
        mSoundEffectsModule.playButtonClickSoundEffect()
        intendedUserAction = UserAction.GO_TO_WELCOME_SCREEN
        if (mInterstitialAd == null)
            handleNavigation()
        else
            mInterstitialAd?.show(requireActivity())
    }

    override fun onPlayAgainClicked(difficultyValue: Int) {
        mSoundEffectsModule.playButtonClickSoundEffect()
        intendedUserAction = UserAction.GO_TO_MAIN_SCREEN
        if (mInterstitialAd == null)
            handleNavigation()
        else
            mInterstitialAd?.show(requireActivity())
    }

    override fun onShareScoreButtonClicked() {
        mSoundEffectsModule.playButtonClickSoundEffect()
        mShareGameScoreUseCase.shareGameScore()
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