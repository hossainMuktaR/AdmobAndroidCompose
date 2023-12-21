package com.hossain.admobandroidcompose.admanager

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions

class NativeAdManager(val context: Context) {
    var mAdLoader: AdLoader? = null
    private var nativeAd: NativeAd? = null

    var status by mutableStateOf(false)

    private fun buildAdLoader(adId: String, callback: (NativeAd) -> Unit) {
        mAdLoader = AdLoader.Builder(context, adId)
            .forNativeAd { ad : NativeAd ->
                // Show the ad.
                nativeAd = ad
                status = !mAdLoader!!.isLoading
                callback(ad)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Handle the failure by logging, altering the UI, and so on.
                    status = false
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    status = true
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                // Methods in the NativeAdOptions.Builder class can be
                // used here to specify individual options settings.
                .build())
            .build()
    }

    fun destroyAd() {
        nativeAd?.destroy()
    }

    fun loadAd(adId: String, callback: (NativeAd) -> Unit) {
        val adRequest = AdRequest.Builder().build()
        buildAdLoader(adId, callback)
        mAdLoader?.loadAd(adRequest)
    }

}