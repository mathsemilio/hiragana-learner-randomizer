package com.mathsemilio.hiraganalearner

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.mathsemilio.hiraganalearner.di.CompositionRoot

@Suppress("unused")
class HiraganaRandomizerApplication : Application() {

    private lateinit var _compositionRoot: CompositionRoot
    val compositionRoot get() = _compositionRoot

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        _compositionRoot = CompositionRoot()
        compositionRoot.getAppThemeUtil(this).setAppThemeFromPreferenceValue()
    }
}