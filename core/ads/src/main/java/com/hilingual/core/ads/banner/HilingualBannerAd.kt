/*
 * Copyright 2026 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
