package com.hossain.admobandroidcompose.admanager

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.hossain.admobandroidcompose.INTERSTITIAL_AD_ID

class InterstitialAdManager(val context: Activity) {
    var interstitialAd: InterstitialAd? = null
    private val TAG = "InterstitialAd"

    var status by mutableStateOf(false)
    fun loadAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, INTERSTITIAL_AD_ID, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    Log.d(TAG, adError.toString() ?: "interstitial ad loaded failed")
                    interstitialAd = null
                    status = false
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    super.onAdLoaded(ad)
                    Log.d(TAG, "Interstitial ad loaded")
                    interstitialAd = ad
                    status = true
                }
            })
    }

    fun showAd() {
        status = false
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                super.onAdClicked()
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                // Called when ad is dismissed.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                interstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.")
                interstitialAd = null
            }

            override fun onAdImpression() {
                super.onAdImpression()
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }
        if (interstitialAd != null) {
            interstitialAd?.show(context)
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }
}