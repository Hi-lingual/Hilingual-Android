package com.hilingual.core.ads.manager

interface AdsPreloadManager {
    fun preloadBanner(adUnitId: String, maxHeight: Int? = null)
    fun preloadInterstitial(adUnitId: String)
}
