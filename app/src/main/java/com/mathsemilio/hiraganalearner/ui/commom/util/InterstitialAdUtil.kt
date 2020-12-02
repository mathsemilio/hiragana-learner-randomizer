package com.mathsemilio.hiraganalearner.ui.commom.util

import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd

fun Fragment.setupAndLoadInterstitialAd(
    adUnitId: String,
    handleOnAdClosedEvent: () -> Unit
): InterstitialAd {
    return InterstitialAd(requireContext()).apply {
        setAdUnitId(adUnitId)
        adListener = (object : AdListener() {
            override fun onAdClosed() {
                handleOnAdClosedEvent()
            }
        })
        loadAd(AdRequest.Builder().build())
    }
}