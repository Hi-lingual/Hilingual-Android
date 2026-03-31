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
package com.hilingual.core.ads.native

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAd
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdLoader
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdLoaderCallback
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdRequest
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdView
import com.hilingual.core.ads.native.component.NativeLineAdContent
import timber.log.Timber

@Composable
internal fun rememberNativeAdState(adUnitId: String): NativeAdState {
    var state by remember { mutableStateOf<NativeAdState>(NativeAdState.Loading) }

    DisposableEffect(adUnitId) {
        var isDisposed = false

        val adRequest = NativeAdRequest.Builder(
            adUnitId = adUnitId,
            nativeAdTypes = listOf(NativeAd.NativeAdType.NATIVE),
        ).build()

        val adCallback = object : NativeAdLoaderCallback {
            override fun onNativeAdLoaded(nativeAd: NativeAd) {
                if (isDisposed) nativeAd.destroy() else state = NativeAdState.Loaded(nativeAd)
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Timber.tag("GMA").e("GMA Next Gen 네이티브 광고 로드 실패: %s", adError)
                if (!isDisposed) state = NativeAdState.Failed
            }
        }

        NativeAdLoader.load(adRequest, adCallback)

        onDispose {
            isDisposed = true
            (state as? NativeAdState.Loaded)?.ad?.destroy()
            state = NativeAdState.Loading
        }
    }

    return state
}

internal fun createNativeAdView(
    context: Context,
    nativeAd: NativeAd,
): NativeAdView {
    val composeView = ComposeView(context).apply {
        setContent {
            NativeLineAdContent(
                title = nativeAd.headline ?: "",
                body = nativeAd.callToAction ?: nativeAd.body ?: "",
            )
        }
    }

    return NativeAdView(context).apply {
        addView(composeView)
        headlineView = composeView
        callToActionView = composeView
        registerNativeAd(nativeAd, null)
    }
}

internal sealed interface NativeAdState {
    data object Loading : NativeAdState
    data class Loaded(val ad: NativeAd) : NativeAdState
    data object Failed : NativeAdState
}
