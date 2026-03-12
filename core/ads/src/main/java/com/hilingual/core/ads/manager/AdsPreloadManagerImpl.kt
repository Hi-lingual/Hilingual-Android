package com.hilingual.core.ads.manager

import android.content.Context
import com.google.android.libraries.ads.mobile.sdk.banner.AdSize
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdPreloader
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdRequest
import com.google.android.libraries.ads.mobile.sdk.common.PreloadConfiguration
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AdsPreloadManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AdsPreloadManager {

    override fun preloadBanner(adUnitId: String, maxHeight: Int?) {
        try {
            val displayMetrics = context.resources.displayMetrics
            val adWidth = (displayMetrics.widthPixels / displayMetrics.density).toInt()

            val adSize = if (maxHeight != null) {
                AdSize.getInlineAdaptiveBannerAdSize(adWidth, maxHeight)
            } else {
                AdSize.getCurrentOrientationInlineAdaptiveBannerAdSize(context, adWidth)
            }

            val adRequest = BannerAdRequest.Builder(adUnitId, adSize).build()
            val preloadConfig = PreloadConfiguration(adRequest)

            BannerAdPreloader.start(adUnitId, preloadConfig)
            Timber.tag("GMA").d("GMA Next Gen 배너 프리로딩 시작: %s", adUnitId)
        } catch (e: Exception) {
            Timber.tag("GMA").e(e, "배너 프리로딩 시작 실패: %s", adUnitId)
        }
    }
}
