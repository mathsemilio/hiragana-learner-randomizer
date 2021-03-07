package com.mathsemilio.hiraganalearner.others

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.mathsemilio.hiraganalearner.common.di.CompositionRoot

class HiraganaRandomizerApplication : Application() {

    private lateinit var _compositionRoot: CompositionRoot
    val compositionRoot get() = _compositionRoot

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(applicationContext)
        _compositionRoot = CompositionRoot()
        _compositionRoot.getAppThemeUtil(applicationContext).setAppThemeFromPreferenceValue()
    }
}