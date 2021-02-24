package com.mathsemilio.hiraganalearner.ui.screens.game.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.google.android.gms.ads.AdRequest
import com.mathsemilio.hiraganalearner.common.ARG_DIFFICULTY_VALUE
import com.mathsemilio.hiraganalearner.common.ARG_SCORE
import com.mathsemilio.hiraganalearner.common.NULL_DIFFICULTY_VALUE_EXCEPTION
import com.mathsemilio.hiraganalearner.common.NULL_SCORE_EXCEPTION
import com.mathsemilio.hiraganalearner.data.repository.PreferencesRepository
import com.mathsemilio.hiraganalearner.others.SoundEffectsModule
import com.mathsemilio.hiraganalearner.ui.common.helper.ScreensNavigator
import com.mathsemilio.hiraganalearner.ui.common.BaseFragment

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

    private lateinit var gameResultScreenView: GameResultScreenViewImpl

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private lateinit var shareGameScoreHelper: ShareGameScoreHelper
    private lateinit var preferencesRepository: PreferencesRepository
    private lateinit var soundEffectsModule: SoundEffectsModule
    private lateinit var screensNavigator: ScreensNavigator

    private lateinit var adRequest: AdRequest

    private var score = 0
    private var difficultyValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        difficultyValue = getDifficultyValue()

        score = getScore()

        shareGameScoreHelper = compositionRoot.shareGameScoreHelper

        preferencesRepository = compositionRoot.preferencesRepository

        soundEffectsModule = compositionRoot.soundEffectsModule

        screensNavigator = compositionRoot.screensNavigator

        adRequest = compositionRoot.adRequest

        onBackPressedCallback = compositionRoot.getOnBackPressedCallback { onHomeButtonClicked() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        gameResultScreenView = compositionRoot.viewFactory.getGameResultScreenView(container)
        return gameResultScreenView.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameResultScreenView.setupUI(score, difficultyValue, preferencesRepository.perfectScoresValue)

        gameResultScreenView.loadBannerAd(adRequest)

        setupOnBackPressedDispatcher()
    }

    private fun getDifficultyValue(): Int {
        return arguments?.getInt(ARG_DIFFICULTY_VALUE) ?: throw RuntimeException(
            NULL_DIFFICULTY_VALUE_EXCEPTION
        )
    }

    private fun getScore(): Int {
        return arguments?.getInt(ARG_SCORE) ?: throw RuntimeException(NULL_SCORE_EXCEPTION)
    }

    private fun setupOnBackPressedDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, onBackPressedCallback
        )
    }

    override fun onHomeButtonClicked() {
        soundEffectsModule.playButtonClickSoundEffect()
        screensNavigator.navigateToWelcomeScreen()
    }

    override fun onPlayAgainClicked(difficultyValue: Int) {
        soundEffectsModule.playButtonClickSoundEffect()
        screensNavigator.navigateToMainScreen(difficultyValue)
    }

    override fun onShareScoreButtonClicked() {
        soundEffectsModule.playButtonClickSoundEffect()
        shareGameScoreHelper.shareGameScore(score)
    }

    override fun onStart() {
        gameResultScreenView.addListener(this)
        super.onStart()
    }

    override fun onStop() {
        gameResultScreenView.removeListener(this)
        super.onStop()
    }
}