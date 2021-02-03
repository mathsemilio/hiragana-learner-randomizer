package com.mathsemilio.hiraganalearner.di

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.mathsemilio.hiraganalearner.data.preferences.repository.PreferencesRepository
import com.mathsemilio.hiraganalearner.others.SoundEffectsModule
import com.mathsemilio.hiraganalearner.ui.others.FragmentContainerHelper
import com.mathsemilio.hiraganalearner.ui.others.ScreensNavigator
import com.mathsemilio.hiraganalearner.ui.others.ViewFactory

class ActivityCompositionRoot(
    private val compositionRoot: CompositionRoot,
    private val activity: AppCompatActivity
) {
    private val _viewFactory by lazy {
        ViewFactory(LayoutInflater.from(context))
    }

    private val _preferencesRepository by lazy {
        PreferencesRepository(context)
    }

    private val _screensNavigator by lazy {
        ScreensNavigator(fragmentManager, activity as FragmentContainerHelper)
    }

    fun getActivity() = activity

    val context get() = activity

    val fragmentManager get() = activity.supportFragmentManager

    val adRequest get() = compositionRoot.getAdRequest()

    val appThemeUtil get() = compositionRoot.getAppThemeUtil(context)

    val viewFactory get() = _viewFactory

    val preferencesRepository get() = _preferencesRepository

    val soundEffectsModule
        get() = SoundEffectsModule(context, preferencesRepository.soundEffectsVolume)

    val screensNavigator get() = _screensNavigator
}