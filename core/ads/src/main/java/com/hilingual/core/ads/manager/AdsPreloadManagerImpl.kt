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
package com.hilingual.core.ads.manager

import android.content.Context
import com.google.android.libraries.ads.mobile.sdk.banner.AdSize
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdPreloader
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdRequest
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest
import com.google.android.libraries.ads.mobile.sdk.common.PreloadConfiguration
import com.google.android.libraries.ads.mobile.sdk.interstitial.InterstitialAdPreloader
import com.hilingual.core.ads.banner.BannerAdType
import com.hilingual.core.ads.utils.screenWidthDp
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

@Singleton
internal class AdsPreloadManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : AdsPreloadManager {

    override fun preloadBanner(type: BannerAdType) {
        try {
            val adWidth = context.screenWidthDp

            val adSize = if (type.maxHeight != null) {
                AdSize.getInlineAdaptiveBannerAdSize(adWidth, type.maxHeight)
            } else {
                AdSize.getCurrentOrientationInlineAdaptiveBannerAdSize(context, adWidth)
            }

            val adRequest = BannerAdRequest.Builder(type.adUnitId, adSize).build()
            val preloadConfig = PreloadConfiguration(adRequest)

            BannerAdPreloader.start(type.adUnitId, preloadConfig)
            Timber.tag("GMA").d("GMA Next Gen 배너 프리로딩 시작: %s", type.adUnitId)
        } catch (e: Exception) {
            Timber.tag("GMA").e(e, "배너 프리로딩 시작 실패: %s", type.adUnitId)
        }
    }

    override fun preloadInterstitial(adUnitId: String) {
        try {
            val adRequest = AdRequest.Builder(adUnitId).build()
            val preloadConfig = PreloadConfiguration(adRequest)

            InterstitialAdPreloader.start(adUnitId, preloadConfig)
            Timber.tag("GMA").d("GMA Next Gen 전면 광고 프리로딩 시작: %s", adUnitId)
        } catch (e: Exception) {
            Timber.tag("GMA").e(e, "전면 광고 프리로딩 실패: %s", adUnitId)
        }
    }
}
