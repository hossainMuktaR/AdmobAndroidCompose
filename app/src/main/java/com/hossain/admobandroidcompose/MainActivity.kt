package com.hossain.admobandroidcompose

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.MobileAds
import com.hossain.admobandroidcompose.admanager.RewardedAdManager
import com.hossain.admobandroidcompose.ui.theme.AdmobAndroidComposeTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the Google Mobile Ads SDK
        MobileAds.initialize(this)
        
        setContent {
            AdmobAndroidComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current as Activity
                    val rewardedAdManager = remember {
                        RewardedAdManager(context)
                    }
                    val interstitialAdStatus = rewardedAdManager.status
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = {
                            if (interstitialAdStatus) {
                                rewardedAdManager.showAd()
                            } else {
                                rewardedAdManager.loadAd()
                            }
                        }) {
                            Text(
                                if (interstitialAdStatus) "Show Interstitial Ad"
                                else "Load Interstitial Ad"
                            )
                        }
                    }
                }
            }
        }
    }
}
