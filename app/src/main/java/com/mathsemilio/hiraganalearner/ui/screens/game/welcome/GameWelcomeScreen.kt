package com.mathsemilio.hiraganalearner.ui.screens.game.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mathsemilio.hiraganalearner.data.preferences.repository.PreferencesRepository
import com.mathsemilio.hiraganalearner.others.soundeffects.SoundEffectsModule
import com.mathsemilio.hiraganalearner.ui.others.ScreensNavigator
import com.mathsemilio.hiraganalearner.ui.screens.common.BaseFragment

class GameWelcomeScreen : BaseFragment(), GameWelcomeScreenView.Listener {

    private lateinit var mView: GameWelcomeScreenViewImpl

    private lateinit var mPreferencesRepository: PreferencesRepository
    private lateinit var mSoundEffectsModule: SoundEffectsModule
    private lateinit var mScreensNavigator: ScreensNavigator

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
    }

    private fun initialize() {
        mPreferencesRepository = getCompositionRoot().getPreferencesRepository()

        mSoundEffectsModule = getCompositionRoot().getSoundEffectsModule(
            mPreferencesRepository.getSoundEffectsVolume()
        )

        mScreensNavigator = getCompositionRoot().getScreensNavigator()
    }

    override fun onPlayClickSoundEffect() {
        mSoundEffectsModule.playClickSoundEffect()
    }

    override fun onSettingsIconClicked() {
        mScreensNavigator.navigateToSettingsScreen()
    }

    override fun onStartButtonClicked(difficultyValue: Int) {
        mSoundEffectsModule.playButtonClickSoundEffect()
        mScreensNavigator.navigateToMainScreen(difficultyValue)
    }

    override fun onStart() {
        mView.registerListener(this)
        super.onStart()
    }

    override fun onDestroyView() {
        mView.removeListener(this)
        super.onDestroyView()
    }
}