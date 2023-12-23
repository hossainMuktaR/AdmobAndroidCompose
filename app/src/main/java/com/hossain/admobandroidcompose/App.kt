package com.hossain.admobandroidcompose

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import com.google.android.gms.ads.MobileAds
import com.hossain.admobandroidcompose.admanager.AppOpenAdManager
import java.util.concurrent.TimeUnit

private const val TAG = "AppOpenAdManager"

class App : MultiDexApplication(), Application.ActivityLifecycleCallbacks, LifecycleEventObserver {

    private lateinit var appOpenAdManager: AppOpenAdManager
    private var currentActivity: Activity? = null


    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
        MobileAds.initialize(this){
        }
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        appOpenAdManager = AppOpenAdManager()
        val isAdLoadDone = appOpenAdManager.adLoadDone.value
        if(isAdLoadDone) {
            Log.d(TAG, "add loaded now showing")
            appOpenAdManager.showAdIfAvailable(currentActivity ?: return)
        }
        createTimer(2000L)
    }

    private fun createTimer(time: Long) {
        val countDownTimer: CountDownTimer =
            object : CountDownTimer(time, 1000) {
                override fun onTick(millisUntilFinished: Long) {
//                    secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1
                }

                override fun onFinish() {
//                    secondsRemaining = 0

                    appOpenAdManager.showAdIfAvailable(
                        currentActivity!!
                    )
                }
            }
        countDownTimer.start()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when(event) {
            Lifecycle.Event.ON_START -> {
                Log.d(TAG, "ShowAdIfAvailable called")
                currentActivity?.let { appOpenAdManager.showAdIfAvailable(it) }
            }

            else -> {}
        }
    }


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        if(!appOpenAdManager.isShowingAd) {
            currentActivity = activity
        }
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}

