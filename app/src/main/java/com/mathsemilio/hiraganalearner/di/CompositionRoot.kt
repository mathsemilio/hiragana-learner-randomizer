package com.mathsemilio.hiraganalearner.di

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.gms.ads.AdRequest
import com.mathsemilio.hiraganalearner.data.preferences.repository.PreferencesRepository
import com.mathsemilio.hiraganalearner.others.soundeffects.SoundEffectsModule
import com.mathsemilio.hiraganalearner.ui.others.*
import com.mathsemilio.hiraganalearner.ui.usecase.ShowInterstitialAdUseCase

class CompositionRoot {

    fun getViewFactory(inflater: LayoutInflater): ViewFactory {
        return ViewFactory(inflater)
    }

    fun getPreferencesRepository(context: Context): PreferencesRepository {
        return PreferencesRepository(context)
    }

    fun getSoundEffectsModule(context: Context, volume: Float): SoundEffectsModule {
        return SoundEffectsModule(context, volume)
    }

    fun getScreensNavigator(
        fragmentManager: FragmentManager,
        fragmentContainerHelper: FragmentContainerHelper
    ): ScreensNavigator {
        return ScreensNavigator(fragmentManager, fragmentContainerHelper)
    }

    fun getShowInterstitialAdUseCase(
        activity: FragmentActivity,
        context: Context
    ): ShowInterstitialAdUseCase {
        return ShowInterstitialAdUseCase(activity, context, getAdRequest())
    }

    fun getAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }

    fun getDialogHelper(context: Context, fragmentManager: FragmentManager): DialogHelper {
        return DialogHelper(context, fragmentManager)
    }

    fun getAppThemeUtil(context: Context): AppThemeUtil {
        return AppThemeUtil(context)
    }
}