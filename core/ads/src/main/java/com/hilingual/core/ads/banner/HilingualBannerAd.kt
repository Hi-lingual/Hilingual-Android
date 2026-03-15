package com.hilingual.core.ads.banner

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import com.hilingual.core.ads.R

@Composable
fun HilingualBannerAd(
    adUnitId: String,
    modifier: Modifier = Modifier,
    maxHeight: Int? = null,
) {
    val adHolder = rememberBannerAdView(adUnitId = adUnitId, maxHeight = maxHeight)

    HilingualBannerAd(
        adHolder = adHolder,
        modifier = modifier,
    )
}

@Composable
fun HilingualBannerAd(
    adHolder: BannerAdHolder,
    modifier: Modifier = Modifier,
) {
    val isPreviewMode = LocalInspectionMode.current

    Box(modifier = modifier.fillMaxWidth()) {
        if (isPreviewMode || !adHolder.isLoaded) {
            Image(
                painter = painterResource(id = R.drawable.loading_feed_and),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
            )
        }

        if (!isPreviewMode && adHolder.isLoaded) {
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { adHolder.adView },
            )
        }
    }
}
