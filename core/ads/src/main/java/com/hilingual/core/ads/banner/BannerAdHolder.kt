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

import android.app.Activity
import android.content.Context
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.libraries.ads.mobile.sdk.banner.AdSize
import com.google.android.libraries.ads.mobile.sdk.banner.AdView
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAd
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdRequest
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.hilingual.core.ads.utils.screenWidthDp
import timber.log.Timber

@ConsistentCopyVisibility
@Stable
data class BannerAdHolder internal constructor(
    internal val adView: AdView,
    private val _isLoaded: State<Boolean>,
    private val _isFailed: State<Boolean>,
) {
    val isLoaded: Boolean get() = _isLoaded.value
    val isFailed: Boolean get() = _isFailed.value
}

@Composable
fun rememberBannerAdView(
    type: BannerAdType,
): BannerAdHolder {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val screenWidth = context.screenWidthDp

    val isLoadedState = remember { mutableStateOf(false) }
    val isFailedState = remember { mutableStateOf(false) }

    val adView = remember {
        createAndLoadAdView(
            context = context,
            activity = activity,
            adUnitId = type.adUnitId,
            screenWidth = screenWidth,
            maxHeight = type.maxHeight,
            onLoaded = { isLoadedState.value = true },
            onFailed = { isFailedState.value = true },
        )
    }

    DisposableEffect(type) {
        onDispose {
            adView.destroy()
        }
    }

    return remember { BannerAdHolder(adView, isLoadedState, isFailedState) }
}

private fun createAndLoadAdView(
    context: Context,
    activity: Activity?,
    adUnitId: String,
    screenWidth: Int,
    maxHeight: Int?,
    onLoaded: () -> Unit,
    onFailed: () -> Unit,
): AdView = AdView(context).apply {
    if (activity == null) {
        Timber.tag("GMA").w("Activity가 null이므로 광고를 로드할 수 없습니다.")
        return@apply
    }

    val adSize = getAdSize(context, screenWidth, maxHeight)
    val adRequest = BannerAdRequest.Builder(adUnitId, adSize).build()

    loadAd(adRequest, createAdLoadCallback(onLoaded, onFailed))
}

private fun createAdLoadCallback(onLoaded: () -> Unit, onFailed: () -> Unit) = object : AdLoadCallback<BannerAd> {
    override fun onAdLoaded(ad: BannerAd) {
        Timber.tag("GMA").d("GMA Next Gen 배너 광고 새로 로드 성공")
        onLoaded()
    }

    override fun onAdFailedToLoad(adError: LoadAdError) {
        Timber.tag("GMA").e("GMA Next Gen 배너 광고 로드 실패: %s", adError)
        onFailed()
    }
}

private fun getAdSize(context: Context, width: Int, maxHeight: Int?): AdSize = if (maxHeight != null) {
    AdSize.getInlineAdaptiveBannerAdSize(width, maxHeight)
} else {
    AdSize.getCurrentOrientationInlineAdaptiveBannerAdSize(context, width)
}
