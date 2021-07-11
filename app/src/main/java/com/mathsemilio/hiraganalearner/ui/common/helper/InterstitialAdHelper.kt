/*
Copyright 2020 Matheus Menezes

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.mathsemilio.hiraganalearner.ui.common.helper

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mathsemilio.hiraganalearner.common.INTERSTITIAL_TEST_AD_ID
import com.mathsemilio.hiraganalearner.common.observable.BaseObservable

class InterstitialAdHelper(
    private val context: Context,
    private val adRequest: AdRequest,
    private val activity: FragmentActivity,
) : BaseObservable<InterstitialAdHelper.Listener>() {

    interface Listener {
        fun onAdDismissed()

        fun onShowAdFailed()
    }

    private var interstitialAd: InterstitialAd? = null

    private val fullScreenContentCallback: FullScreenContentCallback
        get() = object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                notifyAdFailedToShow()
            }

            override fun onAdDismissedFullScreenContent() {
                notifyAdDismissed()
            }
        }

    init {
        loadInterstitialAd()
    }

    private fun loadInterstitialAd() {
        InterstitialAd.load(
            context,
            INTERSTITIAL_TEST_AD_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    this@InterstitialAdHelper.interstitialAd = interstitialAd
                    this@InterstitialAdHelper.interstitialAd?.fullScreenContentCallback =
                        fullScreenContentCallback
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
    }

    fun showInterstitialAd() {
        if (interstitialAd == null)
            notifyAdFailedToShow()
        else
            interstitialAd?.show(activity)
    }

    private fun notifyAdFailedToShow() {
        notifyListener { listener ->
            listener.onShowAdFailed()
        }
    }

    private fun notifyAdDismissed() {
        notifyListener { listener ->
            listener.onAdDismissed()
        }
    }
}