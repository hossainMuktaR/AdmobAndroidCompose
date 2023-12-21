package com.hossain.admobandroidcompose

import android.app.Activity
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.google.android.gms.ads.VideoController
import com.google.android.gms.ads.nativead.NativeAd
import com.hossain.admobandroidcompose.databinding.NativeadViewBinding

@Composable
fun NativeAdView(
    ad: NativeAd,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current as Activity
    AndroidViewBinding(
        factory = NativeadViewBinding::inflate,
        modifier = modifier
    ) {
        val adView = this.nativeAdView.also { nativeAdView ->
            nativeAdView.iconView = this.adAppIcon.apply { setImageDrawable(ad.icon?.drawable)}
            nativeAdView.headlineView = this.adHeadline.apply{ text = ad.headline }
            nativeAdView.advertiserView = this.adAdvertiser.apply { text = ad.advertiser }
            nativeAdView.starRatingView = this.adStars.apply { rating = ad.starRating!!.toFloat() }
            nativeAdView.priceView = this.adPrice.apply { text = ad.price }
            nativeAdView.storeView = this.adStore.apply { text = ad.store }
            nativeAdView.bodyView = this.adBody.apply { text = ad.body }
            nativeAdView.callToActionView = this.adCallToAction.apply { text = ad.callToAction }
            val contentMedia = ad.mediaContent
            val vc = contentMedia?.videoController
            if(vc != null && contentMedia.hasVideoContent()){
                vc.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
                    override fun onVideoEnd() {
                        super.onVideoEnd()
                        Toast.makeText(context, "video playback end", Toast.LENGTH_SHORT).show()
                    }
                }
            }else {
                Toast.makeText(context, "No Video asset", Toast.LENGTH_SHORT).show()
            }
            nativeAdView.mediaView = this.adMedia.apply { mediaContent = ad.mediaContent }
        }
        adView.setNativeAd(ad)
        val parent = this.parent
        parent.removeAllViews()
        parent.addView(adView)

    }
}