package com.mathsemilio.hiraganalearner.di

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.mathsemilio.hiraganalearner.ui.others.AppThemeUtil

class CompositionRoot {

    fun getAdRequest(): AdRequest = AdRequest.Builder().build()

    fun getAppThemeUtil(context: Context) = AppThemeUtil(context)
}