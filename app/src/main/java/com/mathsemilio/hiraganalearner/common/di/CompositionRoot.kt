package com.mathsemilio.hiraganalearner.common.di

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.mathsemilio.hiraganalearner.common.event.poster.EventPoster
import com.mathsemilio.hiraganalearner.ui.common.helper.AppThemeHelper

class CompositionRoot {

    private val _eventPoster by lazy {
        EventPoster()
    }
    val eventPoster get() = _eventPoster

    val adRequest: AdRequest by lazy { AdRequest.Builder().build() }

    fun getAppThemeUtil(context: Context) = AppThemeHelper(context)
}