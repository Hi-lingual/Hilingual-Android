package com.hilingual.core.ads.banner

import com.hilingual.core.ads.BuildConfig

enum class BannerAdType(
    val adUnitId: String,
    val maxHeight: Int? = null,
) {
    BOTTOM_BANNER(
        adUnitId = BuildConfig.ADMOB_BOTTOMBANNER_UNIT_ID,
        maxHeight = 70,
    ),
    INLINE_BANNER(
        adUnitId = BuildConfig.ADMOB_INLINEBANNER_UNIT_ID,
    ),
}
