package com.hilingual.presentation.main

import androidx.lifecycle.ViewModel
import com.hilingual.core.ads.banner.BannerAdType.BOTTOM_BANNER
import com.hilingual.core.ads.banner.BannerAdType.INLINE_BANNER
import com.hilingual.core.ads.manager.AdsPreloadManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val adsPreloadManager: AdsPreloadManager,
) : ViewModel() {

    init {
        preloadMyPageBanner()
        preloadFeedBanner()
    }

    private fun preloadMyPageBanner() = adsPreloadManager.preloadBanner(BOTTOM_BANNER)

    private fun preloadFeedBanner() = adsPreloadManager.preloadBanner(INLINE_BANNER)
}
