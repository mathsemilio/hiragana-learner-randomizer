package com.mathsemilio.hiraganalearner.ui.common.helper

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mathsemilio.hiraganalearner.common.observable.BaseObservable

class InterstitialAdUseHelper(
    private val activity: FragmentActivity,
    private val context: Context,
    private val adRequest: AdRequest,
) : BaseObservable<InterstitialAdUseHelper.Listener>() {

    interface Listener {
        fun onAdDismissed()
        fun onAdFailedToShow()
    }

    private var currentInterstitialAd: InterstitialAd? = null

    init {
        initializeInterstitialAd()
    }

    private fun initializeInterstitialAd() {
        InterstitialAd.load(
            context,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    currentInterstitialAd = interstitialAd
                    currentInterstitialAd?.fullScreenContentCallback =
                        getFullScreenContentCallback()
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    currentInterstitialAd = null
                }
            }
        )
    }

    private fun getFullScreenContentCallback(): FullScreenContentCallback {
        return object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                onShowFailedToShow()
            }

            override fun onAdDismissedFullScreenContent() {
                onAdDismissed()
            }
        }
    }

    fun showInterstitialAd() {
        if (currentInterstitialAd == null)
            onShowFailedToShow()
        else
            currentInterstitialAd?.show(activity)
    }

    private fun onAdDismissed() {
        listeners.forEach { it.onAdDismissed() }
    }

    private fun onShowFailedToShow() {
        listeners.forEach { it.onAdFailedToShow() }
    }
}