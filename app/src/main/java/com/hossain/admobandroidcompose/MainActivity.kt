package com.hossain.admobandroidcompose

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.hossain.admobandroidcompose.admanager.InterstitialAdManager
import com.google.android.gms.ads.MobileAds
import com.hossain.admobandroidcompose.admanager.RewardedAdManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.android.gms.ads.nativead.NativeAd
import com.hossain.admobandroidcompose.admanager.NativeAdManager
import com.hossain.admobandroidcompose.ui.theme.AdmobAndroidComposeTheme


class MainActivity : ComponentActivity() {
    lateinit var nativeAdManager: NativeAdManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the Google Mobile Ads SDK
        MobileAds.initialize(this)
        nativeAdManager = NativeAdManager(this)

        setContent {
            AdmobAndroidComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current as Activity
                    val interstitialAdManager = remember {
                        InterstitialAdManager(context)
                    }
                    val interstitialAdStatus = interstitialAdManager.status
                    var bannerAdView: AdView? = remember {
                        null
                    }
                    val rewardedAdManager = remember {
                        RewardedAdManager(context)
                    }
                    val rewardedAdStatus = rewardedAdManager.status

                    var nativeAd: NativeAd? by remember {
                        mutableStateOf(null)
                    }
                    val nativeAdStatus = nativeAdManager.status
                    var nativeAdShow by remember {
                        mutableStateOf(false)
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = {
                            if (interstitialAdStatus) {
                                interstitialAdManager.showAd()
                            } else {
                                interstitialAdManager.loadAd()
                            }
                        }) {
                            Text(
                                if (interstitialAdStatus) "Show Interstitial Ad"
                                else "Load Interstitial Ad"
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(onClick = {
                            val adRequest = AdRequest.Builder().build()
                            bannerAdView?.loadAd(adRequest)

                        }) {
                            Text("Load Banner Ad")
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(onClick = {
                            if (rewardedAdStatus) {
                                rewardedAdManager.showAd()
                            } else {
                                rewardedAdManager.loadAd()
                            }
                        }) {
                            Text(
                                if (rewardedAdStatus) "Show Rewarded Ad"
                                else "Load Rewarded Ad"
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(onClick = {
                            if(!nativeAdStatus){
                                nativeAdManager.loadAd(
                                    NATIVE_AD_ID
                                ) {
                                    nativeAd = it
                                }
                                nativeAdShow = false
                            } else {
                                nativeAdShow = true
                            }

                        }) {
                            Text(if(nativeAdStatus) "Show Native Ad" else "Load Native Ad")
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        if(nativeAdShow) {
                            NativeAdView(ad = nativeAd!!)
                        }

                    }
                    Box(
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        BannerAdView(
                            adId = BANNER_AD_ID,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            bannerAdView = it
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        nativeAdManager.destroyAd()
    }

}