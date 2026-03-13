package com.hilingual.presentation.main

import androidx.lifecycle.ViewModel
import com.hilingual.core.ads.manager.AdsPreloadManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    adsPreloadManager: AdsPreloadManager,
) : ViewModel() {

    init {
        adsPreloadManager.preloadBanner(
            adUnitId = BuildConfig.ADMOB_BANNER_UNIT_ID,
            maxHeight = 70,
        )
    }
}
