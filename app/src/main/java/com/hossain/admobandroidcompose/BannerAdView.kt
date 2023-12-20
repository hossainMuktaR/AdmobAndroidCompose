package com.hossain.admobandroidcompose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAdView(
    adId: String,
    modifier: Modifier = Modifier,
    updateCallBack: (AdView)-> Unit
) {
    AndroidView(
        factory = {
            AdView(it).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = adId
                updateCallBack(this)
            }
        },
        modifier = modifier
    )
}