package com.hilingual.core.ads.manager

import android.content.Context
import com.google.android.libraries.ads.mobile.sdk.banner.AdSize
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdPreloader
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdRequest
import com.google.android.libraries.ads.mobile.sdk.common.PreloadConfiguration
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
}
