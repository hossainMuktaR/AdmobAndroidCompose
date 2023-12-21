package com.hossain.admobandroidcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.MobileAds
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
                    HomeNativeAd(nativeAdManager)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        nativeAdManager.destroyAd()
    }

}


@Composable
fun HomeNativeAd(
    nativeAdManager: NativeAdManager
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        var nativeAd: NativeAd? by remember {
            mutableStateOf(null)
        }
        val status = nativeAdManager.status
        var adShow by remember {
            mutableStateOf(false)
        }

        Button(onClick = {
            if(!status){
                nativeAdManager.loadAd(
                    NATIVE_AD_ID
                ) {
                    nativeAd = it
                }
                adShow = false
            } else {
                adShow = true
            }

        }) {
            Text(if(status) "Show Native Ad" else "Load Native Ad")
        }
        if(adShow) {
            NativeAdView(ad = nativeAd!!)
        }

    }

}
