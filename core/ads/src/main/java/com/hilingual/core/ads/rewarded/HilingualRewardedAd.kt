package com.hilingual.core.ads.rewarded

import android.app.Activity
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadResult
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest
import com.google.android.libraries.ads.mobile.sdk.common.FullScreenContentError
import com.google.android.libraries.ads.mobile.sdk.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.libraries.ads.mobile.sdk.rewardedinterstitial.RewardedInterstitialAdEventCallback
import timber.log.Timber

suspend fun showRewardedAd(
    activity: Activity,
    adUnitId: String,
    onRewardEarned: () -> Unit,
    onAdDismissed: () -> Unit,
    onAdFailedToLoad: () -> Unit,
) {
    val adRequest = AdRequest.Builder(adUnitId).build()
    Timber.tag("GMA").d("GMA Next Gen 보상형 광고 로드 시작...")

    val runOnMain: (() -> Unit) -> Unit = { callback ->
        activity.runOnUiThread { callback() }
    }

    when (val result = RewardedInterstitialAd.load(adRequest)) {
        is AdLoadResult.Success -> {
            val ad = result.ad
            if (!activity.isFinishing && !activity.isDestroyed) {
                ad.adEventCallback = object : RewardedInterstitialAdEventCallback {
                    override fun onAdDismissedFullScreenContent() {
                        Timber.tag("GMA").d("보상형 광고 닫힘")
                        runOnMain(onAdDismissed)
                    }

                    override fun onAdFailedToShowFullScreenContent(fullScreenContentError: FullScreenContentError) {
                        Timber.tag("GMA").e("보상형 광고 표시 실패: %s", fullScreenContentError)
                        runOnMain(onAdDismissed)
                    }
                }
                ad.show(activity) { rewardItem ->
                    Timber.tag("GMA").d("보상 획득: %s %d", rewardItem.type, rewardItem.amount)
                    runOnMain(onRewardEarned)
                }
            } else {
                Timber.tag("GMA").w("Activity가 이미 종료 상태라 보상형 광고를 표시하지 않습니다.")
                runOnMain(onAdDismissed)
            }
        }

        is AdLoadResult.Failure -> {
            Timber.tag("GMA").e("보상형 광고 로드 실패: %s", result.error)
            runOnMain(onAdFailedToLoad)
        }
    }
}
