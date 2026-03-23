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
