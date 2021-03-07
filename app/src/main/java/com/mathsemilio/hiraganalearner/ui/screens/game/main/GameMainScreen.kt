package com.mathsemilio.hiraganalearner.ui.screens.game.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mathsemilio.hiraganalearner.common.ARG_DIFFICULTY_VALUE
import com.mathsemilio.hiraganalearner.common.NULL_DIFFICULTY_VALUE_EXCEPTION
import com.mathsemilio.hiraganalearner.common.factory.BackPressedCallbackFactory
import com.mathsemilio.hiraganalearner.domain.model.HiraganaSymbol
import com.mathsemilio.hiraganalearner.domain.usecase.GetSymbolUseCase
import com.mathsemilio.hiraganalearner.others.SoundEffectsModule
import com.mathsemilio.hiraganalearner.ui.common.BaseFragment
import com.mathsemilio.hiraganalearner.ui.common.helper.InterstitialAdUseHelper
import com.mathsemilio.hiraganalearner.ui.common.helper.ScreensNavigator

class GameMainScreen : BaseFragment(),
    GameMainScreenView.Listener,
    MainScreenViewModel.Listener,
    AlertUserHelper.Listener,
    GetSymbolUseCase.Listener,
    InterstitialAdUseHelper.Listener {

    companion object {
        fun newInstance(difficultyValue: Int): GameMainScreen {
            val args = Bundle(1).apply { putInt(ARG_DIFFICULTY_VALUE, difficultyValue) }
            val gameMainScreenFragment = GameMainScreen()
            gameMainScreenFragment.arguments = args
            return gameMainScreenFragment
        }
    }

    private lateinit var gameMainScreenView: GameMainScreenViewImpl
    private lateinit var viewModel: MainScreenViewModel

    private lateinit var backPressedCallbackFactory: BackPressedCallbackFactory
    private lateinit var soundEffectsModule: SoundEffectsModule
    private lateinit var screensNavigator: ScreensNavigator
    private lateinit var alertUserHelper: AlertUserHelper
    private lateinit var getSymbolUseCase: GetSymbolUseCase

    private lateinit var interstitialAdUseHelper: InterstitialAdUseHelper

    private var difficultyValue = 0
    private var currentScreenState = ScreenState.TIMER_RUNNING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        difficultyValue = getDifficultyValue()

        viewModel = compositionRoot.mainScreenViewModel

        backPressedCallbackFactory = compositionRoot.backPressedCallbackFactory

        soundEffectsModule = compositionRoot.soundEffectsModule

        getSymbolUseCase = compositionRoot.getSymbolUseCase

        screensNavigator = compositionRoot.screensNavigator

        alertUserHelper = compositionRoot.alertUserHelper

        interstitialAdUseHelper = compositionRoot.interstitialAdHelper
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        gameMainScreenView = compositionRoot.viewFactory.getGameMainScreenView(container)
        return gameMainScreenView.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameMainScreenView.setupUI(difficultyValue)

        viewModel.addListener(this)
        viewModel.startGame(difficultyValue)

        setupOnBackPressedDispatcher()
    }

    private fun getDifficultyValue(): Int {
        return arguments?.getInt(ARG_DIFFICULTY_VALUE) ?: throw RuntimeException(
            NULL_DIFFICULTY_VALUE_EXCEPTION
        )
    }

    private fun setupOnBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallbackFactory.getOnBackPressedCallback { onExitButtonClicked() }
        )
    }

    override fun onExitButtonClicked() {
        alertUserHelper.alertUserOnExitGame(
            { screensNavigator.navigateToWelcomeScreen() },
            { viewModel.resumeGameTimer() })
    }

    override fun onPauseButtonClicked() {
        alertUserHelper.alertUserOnGamePaused { viewModel.resumeGameTimer() }
    }

    override fun onCheckAnswerClicked(selectedRomanization: String) {
        viewModel.checkUserAnswer(selectedRomanization)
    }

    override fun onGameScoreUpdated(newScore: Int) {
        gameMainScreenView.updateGameScoreTextView(newScore)
    }

    override fun onGameProgressUpdated(updatedProgress: Int) {
        gameMainScreenView.updateProgressBarGameProgressValue(updatedProgress)
    }

    override fun onGameCountDownTimeUpdated(updatedCountdownTime: Int) {
        gameMainScreenView.updateProgressBarGameTimerProgressValue(updatedCountdownTime)
    }

    override fun onRomanizationGroupUpdated(updatedRomanizationGroupList: List<String>) {
        gameMainScreenView.updateRomanizationOptionsGroup(updatedRomanizationGroupList)
    }

    override fun onCurrentHiraganaSymbolUpdated(newSymbol: HiraganaSymbol) {
        gameMainScreenView.updateCurrentHiraganaSymbol(newSymbol.symbol)
    }

    override fun onCorrectAnswer() {
        alertUserHelper.alertUserOnCorrectAnswer {
            getSymbolUseCase.getNextSymbol(viewModel.gameFinished, viewModel.score)
            gameMainScreenView.clearRomanizationOptions()
        }
    }

    override fun onWrongAnswer() {
        alertUserHelper.alertUserOnWrongAnswer(viewModel.symbol.romanization) {
            getSymbolUseCase.getNextSymbol(viewModel.gameFinished, viewModel.score)
            gameMainScreenView.clearRomanizationOptions()
        }
    }

    override fun onGameTimeOver() {
        alertUserHelper.alertUserOnTimeOver(viewModel.symbol.romanization) {
            getSymbolUseCase.getNextSymbol(viewModel.gameFinished, viewModel.score)
            gameMainScreenView.clearRomanizationOptions()
        }
    }

    override fun onPauseGameTimer() {
        viewModel.pauseGameTimer()
    }

    override fun onScreenStateChanged(newScreenState: ScreenState) {
        currentScreenState = newScreenState
    }

    override fun playClickSoundEffect() {
        soundEffectsModule.playClickSoundEffect()
    }

    override fun onPlayButtonClickSoundEffect() {
        soundEffectsModule.playButtonClickSoundEffect()
    }

    override fun onPlaySuccessSoundEffect() {
        soundEffectsModule.playSuccessSoundEffect()
    }

    override fun onPlayErrorSoundEffect() {
        soundEffectsModule.playErrorSoundEffect()
    }

    override fun onGetNextSymbol() {
        viewModel.getNextSymbol()
    }

    override fun onShowInterstitialAd() {
        interstitialAdUseHelper.showInterstitialAd()
    }

    override fun onAdDismissed() {
        screensNavigator.navigateToResultScreen(difficultyValue, viewModel.score)
    }

    override fun onAdFailedToShow() {
        screensNavigator.navigateToResultScreen(difficultyValue, viewModel.score)
    }

    override fun onStart() {
        gameMainScreenView.addListener(this)
        alertUserHelper.addListener(this)
        getSymbolUseCase.addListener(this)
        interstitialAdUseHelper.addListener(this)
        super.onStart()
    }

    override fun onResume() {
        if (currentScreenState == ScreenState.TIMER_PAUSED)
            viewModel.resumeGameTimer()

        super.onResume()
    }

    override fun onPause() {
        viewModel.pauseGameTimer()

        if (currentScreenState == ScreenState.TIMER_RUNNING)
            currentScreenState = ScreenState.TIMER_PAUSED

        super.onPause()
    }

    override fun onStop() {
        gameMainScreenView.removeListener(this)
        alertUserHelper.removeListener(this)
        getSymbolUseCase.removeListener(this)
        interstitialAdUseHelper.removeListener(this)
        super.onStop()
    }

    override fun onDestroyView() {
        viewModel.onClearInstance()
        viewModel.removeListener(this)
        super.onDestroyView()
    }
}