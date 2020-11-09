package com.mathsemilio.hiraganalearner

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.mathsemilio.hiraganalearner.others.AppThemeUtil

@Suppress("unused")
class HiraganaRandomizerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {}
        AppThemeUtil(applicationContext).setAppThemeFromPreferenceValue()
    }
}