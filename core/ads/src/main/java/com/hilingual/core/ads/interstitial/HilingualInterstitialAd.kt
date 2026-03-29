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
                    if (!activity.isFinishing && !activity.isDestroyed) {
                        ad.adEventCallback = createEventCallback(onAdDismissed)
                        ad.show(activity)
                    } else {
                        Timber.tag("GMA").w("Activity가 이미 종료 상태라 전면 광고를 표시하지 않습니다.")
                        onAdDismissed()
                    }
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Timber.tag("GMA").e("전면 광고 로드 실패: %s", adError)
                    onAdDismissed()
                }
            },
        )
    }
}

private fun createEventCallback(onAdDismissed: () -> Unit) = object : InterstitialAdEventCallback {
    override fun onAdDismissedFullScreenContent() {
        Timber.tag("GMA").d("전면 광고 닫힘 → 피드백 화면으로 이동")
        onAdDismissed()
    }

    override fun onAdFailedToShowFullScreenContent(fullScreenContentError: FullScreenContentError) {
        Timber.tag("GMA").e("전면 광고 표시 실패: %s", fullScreenContentError)
        onAdDismissed()
    }
}
