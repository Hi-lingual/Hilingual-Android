package com.hilingual.core.ads.banner

import androidx.annotation.DrawableRes
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
    type: BannerAdType,
    modifier: Modifier = Modifier,
) {
    val adHolder = rememberBannerAdView(type = type)

    val placeHolderResId = if (type.maxHeight == 70) {
        R.drawable.loading_mypage_and
    } else {
        R.drawable.loading_feed_and
    }

    HilingualBannerAd(
        adHolder = adHolder,
        modifier = modifier,
        placeHolderResId = placeHolderResId,
    )
}

@Composable
fun HilingualBannerAd(
    adHolder: BannerAdHolder,
    modifier: Modifier = Modifier,
    @DrawableRes placeHolderResId: Int = R.drawable.loading_feed_and,
) {
    val isPreviewMode = LocalInspectionMode.current

    Box(modifier = modifier.fillMaxWidth()) {
        if (isPreviewMode || !adHolder.isLoaded) {
            Image(
                painter = painterResource(id = placeHolderResId),
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
