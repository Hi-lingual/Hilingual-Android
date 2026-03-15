package com.hilingual.core.ads.interstitial

import android.app.Activity
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest
import com.google.android.libraries.ads.mobile.sdk.common.FullScreenContentError
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAd
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAdEventCallback
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAdPreloader
import timber.log.Timber

fun showInterstitialAd(
    activity: Activity,
    adUnitId: String,
    onAdDismissed: () -> Unit,
) {
    val preloadedAd = InterstitialAdPreloader.pollAd(adUnitId)

    if (preloadedAd != null) {
        Timber.tag("GMA").d("프리로드된 전면 광고를 표시합니다.")
        preloadedAd.adEventCallback = createEventCallback(onAdDismissed)
        preloadedAd.show(activity)
    } else {
        Timber.tag("GMA").d("프리로드된 광고 없음, 새로 로드 후 표시합니다.")
        val adRequest = AdRequest.Builder(adUnitId).build()
        InterstitialAd.load(
            adRequest,
            object : AdLoadCallback<InterstitialAd> {
                override fun onAdLoaded(ad: InterstitialAd) {
                    ad.adEventCallback = createEventCallback(onAdDismissed)
                    ad.show(activity)
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Timber.tag("GMA").e("전면 광고 로드 실패: %s", error)
                    onAdDismissed()
                }
            }
        )
    }
}

private fun createEventCallback(onAdDismissed: () -> Unit) = object : InterstitialAdEventCallback {
    override fun onAdDismissedFullScreenContent() {
        Timber.tag("GMA").d("전면 광고 닫힘 → 피드백 화면으로 이동")
        onAdDismissed()
    }

    override fun onAdFailedToShowFullScreenContent(error: FullScreenContentError) {
        Timber.tag("GMA").e("전면 광고 표시 실패: %s", error)
        onAdDismissed()
    }
}
