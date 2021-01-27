package com.mathsemilio.hiraganalearner.ui.screens.game.main

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
import com.mathsemilio.hiraganalearner.common.PERFECT_SCORE
import com.mathsemilio.hiraganalearner.data.preferences.repository.PreferencesRepository
import com.mathsemilio.hiraganalearner.domain.hiragana.HiraganaSymbol
import com.mathsemilio.hiraganalearner.others.soundeffects.SoundEffectsModule
import com.mathsemilio.hiraganalearner.ui.others.DialogHelper
import com.mathsemilio.hiraganalearner.ui.others.ScreensNavigator
import com.mathsemilio.hiraganalearner.ui.screens.common.BaseFragment
import com.mathsemilio.hiraganalearner.ui.screens.game.main.usecase.AlertUserUseCase
import com.mathsemilio.hiraganalearner.ui.screens.game.main.usecase.AlertUserUseCaseEventListener
import com.mathsemilio.hiraganalearner.ui.screens.game.main.viewmodel.GameMainScreenViewModel
import com.mathsemilio.hiraganalearner.ui.screens.game.main.viewmodel.ViewModelEventListener

class GameMainScreen : BaseFragment(), GameMainScreenView.Listener, ViewModelEventListener,
    AlertUserUseCaseEventListener {

    companion object {
        fun newInstance(difficultyValue: Int): GameMainScreen {
            val args = Bundle().apply { putInt(ARG_DIFFICULTY_VALUE, difficultyValue) }
            val gameMainScreenFragment = GameMainScreen()
            gameMainScreenFragment.arguments = args
            return gameMainScreenFragment
        }
    }

    private lateinit var mView: GameMainScreenViewImpl
    private lateinit var mViewModel: GameMainScreenViewModel

    private lateinit var mPreferencesRepository: PreferencesRepository
    private lateinit var mSoundEffectsModule: SoundEffectsModule
    private lateinit var mScreensNavigator: ScreensNavigator
    private lateinit var mAlertUserUseCase: AlertUserUseCase
    private lateinit var mDialogHelper: DialogHelper

    private var mInterstitialAd: InterstitialAd? = null
    private lateinit var mAdRequest: AdRequest

    private var mDifficultyValue = 0
    private var mCurrentControllerState = ControllerState.RUNNING

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView = getCompositionRoot().getViewFactory().getGameMainScreenView(container)
        return mView.getRootView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialize()

        mView.onControllerViewCreated(mDifficultyValue)

        registerListeners()

        mViewModel.startGame(mDifficultyValue)

        initializeInterstitialAd()
    }

    private fun initialize() {
        mDifficultyValue = getDifficultyValue()

        mViewModel = getCompositionRoot().getGameMainScreenViewModel()

        mPreferencesRepository = getCompositionRoot().getPreferencesRepository()

        mSoundEffectsModule = getCompositionRoot().getSoundEffectsModule(
            mPreferencesRepository.getSoundEffectsVolume()
        )

        mScreensNavigator = getCompositionRoot().getScreensNavigator()

        mAlertUserUseCase = getCompositionRoot().getAlertUserUseCase()

        mDialogHelper = getCompositionRoot().getDialogHelper()

        mAdRequest = getCompositionRoot().getAdRequest()

        getCompositionRoot().getBackPressedDispatcher { onExitButtonClicked() }
    }

    private fun registerListeners() {
        mView.registerListener(this)
        mViewModel.registerListener(this)
        mAlertUserUseCase.registerListener(this)
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
                mScreensNavigator.navigateToResultScreen(
                    mDifficultyValue,
                    mViewModel.getGameScore()
                )
            }

            override fun onAdDismissedFullScreenContent() {
                mScreensNavigator.navigateToResultScreen(
                    mDifficultyValue,
                    mViewModel.getGameScore()
                )
            }
        }
    }

    private fun getDifficultyValue(): Int {
        return arguments?.getInt(ARG_DIFFICULTY_VALUE) ?: 0
    }

    private fun checkIfGameIsFinished() {
        when (mViewModel.gameFinished) {
            true -> {
                if (mViewModel.getGameScore() == PERFECT_SCORE)
                    mPreferencesRepository.incrementPerfectScoresValue()

                showAdAndNavigate()
            }
            false -> mViewModel.getNextSymbol()
        }
    }

    private fun showAdAndNavigate() {
        if (mInterstitialAd == null)
            mScreensNavigator.navigateToResultScreen(mDifficultyValue, mViewModel.getGameScore())
        else
            mInterstitialAd?.show(requireActivity())
    }

    override fun playClickSoundEffect() {
        mSoundEffectsModule.playClickSoundEffect()
    }

    override fun onExitButtonClicked() {
        mAlertUserUseCase.alertUserOnExitGame(
            { mScreensNavigator.navigateToWelcomeScreen() },
            { mViewModel.resumeGameTimer() })
    }

    override fun onPauseButtonClicked() {
        mAlertUserUseCase.alertUserOnGamePaused { mViewModel.resumeGameTimer() }
    }

    override fun onCheckAnswerClicked(selectedRomanization: String) {
        mViewModel.checkUserAnswer(selectedRomanization)
    }

    override fun onGameScoreUpdated(newScore: Int) {
        mView.updateGameScoreTextView(newScore)
    }

    override fun onGameProgressUpdated(updatedProgress: Int) {
        mView.updateProgressBarGameProgressValue(updatedProgress)
    }

    override fun onGameCountDownTimeUpdated(updatedCountdownTime: Int) {
        mView.updateProgressBarGameTimerProgressValue(updatedCountdownTime)
    }

    override fun onRomanizationGroupUpdated(updatedRomanizationGroupList: List<String>) {
        mView.updateRomanizationOptionsGroup(updatedRomanizationGroupList)
    }

    override fun onCurrentHiraganaSymbolUpdated(newSymbol: HiraganaSymbol) {
        mView.updateCurrentHiraganaSymbol(newSymbol.symbol)
    }

    override fun onCorrectAnswer() {
        mAlertUserUseCase.alertUserOnCorrectAnswer {
            checkIfGameIsFinished()
            mView.clearRomanizationOptions()
        }
    }

    override fun onWrongAnswer() {
        mAlertUserUseCase.alertUserOnWrongAnswer(mViewModel.getCurrentSymbol().romanization) {
            checkIfGameIsFinished()
            mView.clearRomanizationOptions()
        }
    }

    override fun onGameTimeOver() {
        mAlertUserUseCase.alertUserOnTimeOver(mViewModel.getCurrentSymbol().romanization) {
            checkIfGameIsFinished()
        }
    }

    override fun onPauseGameTimer() {
        mViewModel.pauseGameTimer()
    }

    override fun onControllerStateChanged(newControllerState: ControllerState) {
        mCurrentControllerState = newControllerState
    }

    override fun onPlayButtonClickSoundEffect() {
        mSoundEffectsModule.playButtonClickSoundEffect()
    }

    override fun onPlaySuccessSoundEffect() {
        mSoundEffectsModule.playSuccessSoundEffect()
    }

    override fun onPlayErrorSoundEffect() {
        mSoundEffectsModule.playErrorSoundEffect()
    }

    override fun onPause() {
        mViewModel.pauseGameTimer()

        if (mCurrentControllerState == ControllerState.RUNNING)
            mCurrentControllerState = ControllerState.PAUSED

        super.onPause()
    }

    override fun onResume() {
        if (mCurrentControllerState == ControllerState.PAUSED)
            mViewModel.resumeGameTimer()

        super.onResume()
    }

    override fun onDestroyView() {
        mView.removeListener(this)
        mViewModel.onControllerOnDestroyView()
        mViewModel.removeListener(this)
        mAlertUserUseCase.removeListener(this)
        mInterstitialAd = null
        super.onDestroyView()
    }
}