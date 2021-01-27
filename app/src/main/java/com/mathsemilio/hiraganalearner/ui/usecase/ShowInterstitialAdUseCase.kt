package com.mathsemilio.hiraganalearner.ui.usecase

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mathsemilio.hiraganalearner.R
import com.mathsemilio.hiraganalearner.common.BaseObservable

class ShowInterstitialAdUseCase(
    private val activity: FragmentActivity,
    private val context: Context,
    private val adRequest: AdRequest,
) : BaseObservable<OnInterstitialAdEventListener>() {

    private lateinit var mInterstitialAd: InterstitialAd

    init {
        initializeInterstitialAd()
    }

    private fun initializeInterstitialAd() {
        InterstitialAd.load(
            context,
            context.getString(R.string.interstitialAdTestUnitId),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    mInterstitialAd.fullScreenContentCallback = getFullScreenContentCallback()
                }
            }
        )
    }

    private fun getFullScreenContentCallback(): FullScreenContentCallback {
        return object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(adError: AdError?) = showInterstitialAdFailed()
            override fun onAdDismissedFullScreenContent() = onInterstitialAdDismissed()
        }
    }

    fun showInterstitialAd() = mInterstitialAd.show(activity)

    private fun showInterstitialAdFailed() {
        getListeners().forEach { it.onShowInterstitialAdFailed() }
    }

    private fun onInterstitialAdDismissed() {
        getListeners().forEach { it.onInterstitialAdDismissed() }
    }
}