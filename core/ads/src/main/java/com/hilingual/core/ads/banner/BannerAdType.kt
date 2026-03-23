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
