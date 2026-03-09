package com.hilingual.core.ads.banner

import android.app.Activity
import android.content.Context
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.libraries.ads.mobile.sdk.banner.AdSize
import com.google.android.libraries.ads.mobile.sdk.banner.AdView
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAd
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdPreloader
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdRequest
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.hilingual.core.ads.BuildConfig
import com.hilingual.core.ads.R
import timber.log.Timber

@Composable
fun HilingualBannerAd(
    modifier: Modifier = Modifier,
    adUnitId: String = BuildConfig.ADMOB_BANNER_UNIT_ID,
    maxHeight: Int? = null
) {
    val activity = LocalActivity.current
    val isPreviewMode = LocalInspectionMode.current
    val screenWidth = with(LocalDensity.current) { LocalWindowInfo.current.containerSize.width.toDp().value.toInt() }

    var isAdLoaded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        if (isPreviewMode || !isAdLoaded) {
            Image(
                painter = painterResource(id = R.drawable.loading_feed_and),
                contentDescription = "Loading Ad",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }

        if (!isPreviewMode && activity != null && screenWidth > 0) {
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { context ->
                    createAndLoadAdView(
                        context = context,
                        activity = activity,
                        adUnitId = adUnitId,
                        screenWidth = screenWidth,
                        maxHeight = maxHeight,
                        onLoaded = { isAdLoaded = true }
                    )
                },
                onRelease = { adView ->
                    Timber.tag("GMA").d("배너 광고 리소스 해제(destroy)")
                    adView.destroy()
                }
            )
        }
    }
}

private fun createAndLoadAdView(
    context: Context,
    activity: Activity,
    adUnitId: String,
    screenWidth: Int,
    maxHeight: Int?,
    onLoaded: () -> Unit
): AdView {
    return AdView(context).apply {
        val preloadedAd = BannerAdPreloader.pollAd(adUnitId)
        if (preloadedAd != null) {
            Timber.tag("GMA").d("프리로드된 배너 광고를 화면에 등록합니다.")
            registerBannerAd(preloadedAd, activity)
            onLoaded()
        } else {
            Timber.tag("GMA").d("프리로드된 광고가 없어 새로 로드를 요청합니다.")
            val adSize = getAdSize(context, screenWidth, maxHeight)
            val adRequest = BannerAdRequest.Builder(adUnitId, adSize).build()
            
            loadAd(adRequest, createAdLoadCallback(onLoaded))
        }
    }
}

private fun createAdLoadCallback(onLoaded: () -> Unit) = object : AdLoadCallback<BannerAd> {
    override fun onAdLoaded(ad: BannerAd) {
        Timber.tag("GMA").d("GMA Next Gen 배너 광고 새로 로드 성공")
        onLoaded()
    }

    override fun onAdFailedToLoad(adError: LoadAdError) {
        Timber.tag("GMA").e("GMA Next Gen 배너 광고 로드 실패: %s", adError)
    }
}

private fun getAdSize(context: Context, width: Int, maxHeight: Int?): AdSize {
    return if (maxHeight != null) {
        AdSize.getInlineAdaptiveBannerAdSize(width, maxHeight)
    } else {
        AdSize.getCurrentOrientationInlineAdaptiveBannerAdSize(context, width)
    }
}
