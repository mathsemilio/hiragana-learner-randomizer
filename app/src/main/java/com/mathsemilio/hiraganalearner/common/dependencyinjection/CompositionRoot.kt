package com.mathsemilio.hiraganalearner.common.dependencyinjection

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.mathsemilio.hiraganalearner.ui.common.helper.AppThemeHelper

class CompositionRoot {

    val adRequest: AdRequest by lazy { AdRequest.Builder().build() }

    fun getAppThemeUtil(context: Context) = AppThemeHelper(context)
}