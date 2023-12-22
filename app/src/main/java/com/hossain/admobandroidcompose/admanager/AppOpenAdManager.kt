package com.hossain.admobandroidcompose.admanager

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.hossain.admobandroidcompose.APP_OPEN_AD_ID
import java.util.Date

private const val TAG = "AppOpenAdManager"



class AppOpenAdManager {
    /** Keep track of the time an app open ad is loaded to ensure you don't show an expired ad. */
    private var loadTime: Long = 0

    /** Interface definition for a callback to be invoked when an app open ad is complete. */
    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
    }
    var appOpenAd: AppOpenAd? = null
    private var isLoadingAd = false
    var isShowingAd = false
    var adLoadDone = mutableStateOf(false)

    fun loadAd(context: Context) {
        if(isLoadingAd || isAdAvailable()) {
            return
        }
        isLoadingAd = true
        val request = AdRequest.Builder().build()
        AppOpenAd.load(
            context,
            APP_OPEN_AD_ID,
            request,
            object : AppOpenAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    // Called when an app open ad has failed to load.
                    Log.d(TAG, loadAdError.message)
                    isLoadingAd = false;
                }

                override fun onAdLoaded(ad: AppOpenAd) {
                    super.onAdLoaded(ad)
                    // Called when an app open ad has loaded.
                    Log.d(TAG, "Ad was loaded.")
                    appOpenAd = ad
                    adLoadDone.value = true
                    isLoadingAd = false
                    loadTime = Date().time
                }
            }
        )

    }

    fun showAdIfAvailable(
        activity: Activity,
        onShowAdCompleteListener: OnShowAdCompleteListener
    ) {
        if(isShowingAd) {
            Log.d(TAG, "The app open ad is already showing.")
            return
        }
        // If the app open ad is not available yet, invoke the callback then load the ad.
        if (!isAdAvailable()) {
            Log.d(TAG, "The app open ad is not ready yet.")
            onShowAdCompleteListener.onShowAdComplete()
            loadAd(activity)
            return
        }
        appOpenAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                // Called when full screen content is dismissed.
                // Set the reference to null so isAdAvailable() returns false.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                appOpenAd = null
                isShowingAd = false

                onShowAdCompleteListener.onShowAdComplete()
                loadAd(activity)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                super.onAdFailedToShowFullScreenContent(adError)
                // Called when fullscreen content failed to show.
                // Set the reference to null so isAdAvailable() returns false.
                Log.d(TAG, adError.message)
                appOpenAd = null
                isShowingAd = false

                onShowAdCompleteListener.onShowAdComplete()
                loadAd(activity)
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                // Called when fullscreen content is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }
        isShowingAd = true
        appOpenAd!!.let {
            it.show(activity)
            Log.d(TAG, "AppOpenAd.Show called")
        }
    }
    /** Show the ad if one isn't already showing. */
    fun showAdIfAvailable(activity: Activity) {
        showAdIfAvailable(
            activity,
            object : OnShowAdCompleteListener {
                override fun onShowAdComplete() {
                    // Empty because the user will go back to the activity that shows the ad.
                }
            })
    }


    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long) : Boolean{
        val dateDifference: Long = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    private fun isAdAvailable(): Boolean {
        return appOpenAd != null
//                && wasLoadTimeLessThanNHoursAgo(4)
    }
}