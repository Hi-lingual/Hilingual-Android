package com.hilingual.presentation.main

import androidx.lifecycle.ViewModel
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

    private fun preloadMyPageBanner() = adsPreloadManager.preloadBanner(BuildConfig.ADMOB_BOTTOMBANNER_UNIT_ID, 70)

    private fun preloadFeedBanner() = adsPreloadManager.preloadBanner(BuildConfig.ADMOB_INLINEBANNER_UNIT_ID)
}
