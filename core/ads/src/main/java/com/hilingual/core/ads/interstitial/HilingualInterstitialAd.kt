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
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadResult
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest
import com.google.android.libraries.ads.mobile.sdk.common.FullScreenContentError
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAd
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAdEventCallback
import timber.log.Timber

suspend fun showInterstitialAd(
    activity: Activity,
    adUnitId: String,
    onAdDismissed: () -> Unit,
) {
    val adRequest = AdRequest.Builder(adUnitId).build()
    Timber.tag("GMA").d("GMA Next Gen 전면 광고 로드 시작...")

    val runOnMain: (() -> Unit) -> Unit = { callback ->
        activity.runOnUiThread { callback() }
    }

    when (val result = InterstitialAd.load(adRequest)) {
        is AdLoadResult.Success -> {
            val ad = result.ad
            if (!activity.isFinishing && !activity.isDestroyed) {
                ad.adEventCallback = object : InterstitialAdEventCallback {
                    override fun onAdDismissedFullScreenContent() {
                        Timber.tag("GMA").d("전면 광고 닫힘 → 피드백 화면으로 이동")
                        runOnMain(onAdDismissed)
                    }

                    override fun onAdFailedToShowFullScreenContent(fullScreenContentError: FullScreenContentError) {
                        Timber.tag("GMA").e("전면 광고 표시 실패: %s", fullScreenContentError)
                        runOnMain(onAdDismissed)
                    }
                }
                ad.show(activity)
            } else {
                Timber.tag("GMA").w("Activity가 이미 종료 상태라 전면 광고를 표시하지 않습니다.")
                runOnMain(onAdDismissed)
            }
        }
        is AdLoadResult.Failure -> {
            Timber.tag("GMA").e("전면 광고 로드 실패: %s", result.error)
            runOnMain(onAdDismissed)
        }
    }
}
