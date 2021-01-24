package com.mathsemilio.hiraganalearner.others

import android.app.Application
import com.mathsemilio.hiraganalearner.di.CompositionRoot

class HiraganaRandomizerApplication : Application() {

    private lateinit var _compositionRoot: CompositionRoot
    val compositionRoot get() = _compositionRoot

    override fun onCreate() {
        super.onCreate()
        _compositionRoot = CompositionRoot()
        _compositionRoot.getAppThemeUtil(applicationContext).setAppThemeFromPreferenceValue()
    }
}