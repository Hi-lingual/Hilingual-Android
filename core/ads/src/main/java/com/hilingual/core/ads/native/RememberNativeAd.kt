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
internal fun rememberNativeAd(adUnitId: String): NativeAd? {
    var loadedAdState by remember { mutableStateOf<NativeAd?>(null) }

    DisposableEffect(adUnitId) {
        val adRequest = NativeAdRequest.Builder(
            adUnitId = adUnitId,
            nativeAdTypes = listOf(NativeAd.NativeAdType.NATIVE),
        ).build()

        val adCallback = object : NativeAdLoaderCallback {
            override fun onNativeAdLoaded(nativeAd: NativeAd) {
                loadedAdState = nativeAd
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Timber.tag("GMA").e("GMA Next Gen 네이티브 광고 로드 실패: %s", adError)
            }
        }

        NativeAdLoader.load(adRequest, adCallback)

        onDispose {
            loadedAdState?.destroy()
        }
    }

    return loadedAdState
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
