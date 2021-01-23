package com.mathsemilio.hiraganalearner.di

import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.mathsemilio.hiraganalearner.data.preferences.repository.PreferencesRepository
import com.mathsemilio.hiraganalearner.others.TrainingNotificationHelper
import com.mathsemilio.hiraganalearner.others.soundeffects.HiraganaSoundsModule
import com.mathsemilio.hiraganalearner.others.soundeffects.SoundEffectsModule
import com.mathsemilio.hiraganalearner.ui.others.*
import com.mathsemilio.hiraganalearner.ui.screens.game.main.usecase.AlertUserUseCase
import com.mathsemilio.hiraganalearner.ui.screens.game.main.viewmodel.GameMainScreenViewModel
import com.mathsemilio.hiraganalearner.ui.screens.game.result.usecase.ShareGameScoreUseCase

class ControllerCompositionRoot(
    private val mCompositionRoot: CompositionRoot,
    private val mFragment: Fragment
) {
    private fun getLayoutInflater(): LayoutInflater {
        return LayoutInflater.from(mFragment.requireContext())
    }

    private fun getFragmentContainerHelper(): FragmentContainerHelper {
        return mFragment.requireActivity() as FragmentContainerHelper
    }

    fun getViewFactory(): ViewFactory {
        return mCompositionRoot.getViewFactory(getLayoutInflater())
    }

    fun getPreferencesRepository(): PreferencesRepository {
        return mCompositionRoot.getPreferencesRepository(mFragment.requireContext())
    }

    fun getSoundEffectsModule(volume: Float): SoundEffectsModule {
        return mCompositionRoot.getSoundEffectsModule(mFragment.requireContext(), volume)
    }

    fun getHiraganaSoundsModule(volume: Float): HiraganaSoundsModule {
        return HiraganaSoundsModule(mFragment.requireContext(), volume)
    }

    fun getScreensNavigator(): ScreensNavigator {
        return mCompositionRoot.getScreensNavigator(
            mFragment.parentFragmentManager, getFragmentContainerHelper()
        )
    }

    fun getTrainingNotificationHelper(): TrainingNotificationHelper {
        return TrainingNotificationHelper(mFragment.requireContext())
    }

    fun getAppToolbarHelper(): AppToolbarHelper {
        return mFragment.requireActivity() as AppToolbarHelper
    }

    fun getMessagesHelper(): MessagesHelper {
        return MessagesHelper(mFragment.requireContext())
    }

    fun getDialogHelper(): DialogHelper {
        return mCompositionRoot.getDialogHelper(
            mFragment.requireContext(),
            mFragment.parentFragmentManager
        )
    }

    fun getAppThemeUtil(): AppThemeUtil {
        return mCompositionRoot.getAppThemeUtil(mFragment.requireContext())
    }

    fun getGameMainScreenViewModel(): GameMainScreenViewModel {
        return GameMainScreenViewModel()
    }

    fun getBackPressedDispatcher(onBackPressed: () -> Unit) {
        mFragment.requireActivity().onBackPressedDispatcher.addCallback(
            mFragment.viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() = onBackPressed()
            }
        )
    }

    fun getAlertUserUseCase(): AlertUserUseCase {
        return AlertUserUseCase(mFragment.requireContext(), mFragment.parentFragmentManager)
    }

    fun getShareGameScoreUseCase(score: Int): ShareGameScoreUseCase {
        return ShareGameScoreUseCase(mFragment.requireContext(), score)
    }
}