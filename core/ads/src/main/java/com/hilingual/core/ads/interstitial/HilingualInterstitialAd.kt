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

enum class InterstitialAdResult {
    /** 광고가 노출되고 사용자가 닫음 */
    DISMISSED,

    /** 로드 실패, 표시 실패, 또는 Activity 종료로 노출되지 못함 */
    FAILED,
}

suspend fun showInterstitialAd(
    activity: Activity,
    adUnitId: String,
    onAdFinished: (InterstitialAdResult) -> Unit,
) {
    val adRequest = AdRequest.Builder(adUnitId).build()
    Timber.tag("GMA").d("GMA Next Gen 전면 광고 로드 시작...")

    val finishOnMain: (InterstitialAdResult) -> Unit = { result ->
        activity.runOnUiThread { onAdFinished(result) }
    }

    when (val result = InterstitialAd.load(adRequest)) {
        is AdLoadResult.Success -> {
            val ad = result.ad
            if (!activity.isFinishing && !activity.isDestroyed) {
                ad.adEventCallback = object : InterstitialAdEventCallback {
                    override fun onAdDismissedFullScreenContent() {
                        Timber.tag("GMA").d("전면 광고 닫힘 → 피드백 화면으로 이동")
                        finishOnMain(InterstitialAdResult.DISMISSED)
                    }

                    override fun onAdFailedToShowFullScreenContent(fullScreenContentError: FullScreenContentError) {
                        Timber.tag("GMA").e("전면 광고 표시 실패: %s", fullScreenContentError)
                        finishOnMain(InterstitialAdResult.FAILED)
                    }
                }
                ad.show(activity)
            } else {
                Timber.tag("GMA").w("Activity가 이미 종료 상태라 전면 광고를 표시하지 않습니다.")
                finishOnMain(InterstitialAdResult.FAILED)
            }
        }

        is AdLoadResult.Failure -> {
            Timber.tag("GMA").e("전면 광고 로드 실패: %s", result.error)
            finishOnMain(InterstitialAdResult.FAILED)
        }
    }
}
