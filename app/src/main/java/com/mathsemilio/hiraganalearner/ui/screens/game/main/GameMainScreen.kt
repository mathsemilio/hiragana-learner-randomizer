package com.mathsemilio.hiraganalearner.ui.screens.game.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.google.android.gms.ads.AdRequest
import com.mathsemilio.hiraganalearner.common.ARG_DIFFICULTY_VALUE
import com.mathsemilio.hiraganalearner.common.NULL_DIFFICULTY_VALUE_EXCEPTION
import com.mathsemilio.hiraganalearner.domain.hiragana.HiraganaSymbol
import com.mathsemilio.hiraganalearner.game.model.GameModel
import com.mathsemilio.hiraganalearner.others.SoundEffectsModule
import com.mathsemilio.hiraganalearner.ui.others.ScreensNavigator
import com.mathsemilio.hiraganalearner.ui.screens.common.BaseFragment
import com.mathsemilio.hiraganalearner.ui.screens.common.InterstitialAdUseCase
import com.mathsemilio.hiraganalearner.ui.screens.game.main.usecase.AlertUserUseCase
import com.mathsemilio.hiraganalearner.ui.screens.game.main.usecase.GetSymbolUseCase

class GameMainScreen : BaseFragment(),
    GameMainScreenView.Listener,
    GameModel.Listener,
    AlertUserUseCase.Listener,
    GetSymbolUseCase.Listener,
    InterstitialAdUseCase.Listener {

    companion object {
        fun newInstance(difficultyValue: Int): GameMainScreen {
            val args = Bundle().apply { putInt(ARG_DIFFICULTY_VALUE, difficultyValue) }
            val gameMainScreenFragment = GameMainScreen()
            gameMainScreenFragment.arguments = args
            return gameMainScreenFragment
        }
    }

    private lateinit var gameMainScreenView: GameMainScreenViewImpl
    private lateinit var model: GameModel

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private lateinit var soundEffectsModule: SoundEffectsModule
    private lateinit var screensNavigator: ScreensNavigator
    private lateinit var alertUserUseCase: AlertUserUseCase
    private lateinit var getSymbolUseCase: GetSymbolUseCase

    private lateinit var interstitialAdUseCase: InterstitialAdUseCase
    private lateinit var adRequest: AdRequest

    private var difficultyValue = 0
    private var currentScreenState = ScreenState.TIMER_RUNNING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        difficultyValue = getDifficultyValue()

        model = compositionRoot.gameModel

        onBackPressedCallback = compositionRoot.getOnBackPressedCallback { onExitButtonClicked() }

        soundEffectsModule = compositionRoot.soundEffectsModule

        getSymbolUseCase = compositionRoot.getSymbolUseCase

        screensNavigator = compositionRoot.screensNavigator

        alertUserUseCase = compositionRoot.alertUserUseCase

        adRequest = compositionRoot.adRequest

        interstitialAdUseCase = compositionRoot.interstitialAdUseCase
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        gameMainScreenView = compositionRoot.viewFactory.getGameMainScreenView(container)
        return gameMainScreenView.getRootView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameMainScreenView.setupUI(difficultyValue)

        model.addListener(this)

        model.startGame(difficultyValue)

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
            onBackPressedCallback
        )
    }

    override fun onExitButtonClicked() {
        alertUserUseCase.alertUserOnExitGame(
            { screensNavigator.navigateToWelcomeScreen() },
            { model.resumeGameTimer() })
    }

    override fun onPauseButtonClicked() {
        alertUserUseCase.alertUserOnGamePaused { model.resumeGameTimer() }
    }

    override fun onCheckAnswerClicked(selectedRomanization: String) {
        model.checkUserAnswer(selectedRomanization)
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
        alertUserUseCase.alertUserOnCorrectAnswer {
            getSymbolUseCase.getNextSymbol(model.gameFinished, model.score)
            gameMainScreenView.clearRomanizationOptions()
        }
    }

    override fun onWrongAnswer() {
        alertUserUseCase.alertUserOnWrongAnswer(model.symbol.romanization) {
            getSymbolUseCase.getNextSymbol(model.gameFinished, model.score)
            gameMainScreenView.clearRomanizationOptions()
        }
    }

    override fun onGameTimeOver() {
        alertUserUseCase.alertUserOnTimeOver(model.symbol.romanization) {
            getSymbolUseCase.getNextSymbol(model.gameFinished, model.score)
            gameMainScreenView.clearRomanizationOptions()
        }
    }

    override fun onPauseGameTimer() {
        model.pauseGameTimer()
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
        model.getNextSymbol()
    }

    override fun onShowInterstitialAd() {
        interstitialAdUseCase.showInterstitialAd()
    }

    override fun onAdDismissed() {
        screensNavigator.navigateToResultScreen(difficultyValue, model.score)
    }

    override fun onAdFailedToShow() {
        screensNavigator.navigateToResultScreen(difficultyValue, model.score)
    }

    override fun onPause() {
        model.pauseGameTimer()

        if (currentScreenState == ScreenState.TIMER_RUNNING)
            currentScreenState = ScreenState.TIMER_PAUSED

        super.onPause()
    }

    override fun onStop() {
        gameMainScreenView.removeListener(this)
        alertUserUseCase.removeListener(this)
        getSymbolUseCase.removeListener(this)
        interstitialAdUseCase.removeListener(this)
        super.onStop()
    }

    override fun onDestroyView() {
        model.onClearInstance()
        model.removeListener(this)
        super.onDestroyView()
    }

    override fun onStart() {
        gameMainScreenView.addListener(this)
        alertUserUseCase.addListener(this)
        getSymbolUseCase.addListener(this)
        interstitialAdUseCase.addListener(this)
        super.onStart()
    }

    override fun onResume() {
        if (currentScreenState == ScreenState.TIMER_PAUSED)
            model.resumeGameTimer()

        super.onResume()
    }
}