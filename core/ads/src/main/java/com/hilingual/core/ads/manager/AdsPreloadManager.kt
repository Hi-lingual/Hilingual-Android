package com.hilingual.core.ads.manager

import com.hilingual.core.ads.banner.BannerAdType

interface AdsPreloadManager {
    fun preloadBanner(type: BannerAdType)
    fun preloadInterstitial(adUnitId: String)
}
