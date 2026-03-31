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
package com.hilingual.core.ads.native

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.hilingual.core.ads.native.component.NativeLineAdContent
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualNativeLineAd(
    adUnitId: String,
    modifier: Modifier = Modifier,
) {
    val state = if (LocalInspectionMode.current) NativeAdState.Loading else rememberNativeAdState(adUnitId)

    when (state) {
        NativeAdState.Loading -> NativeLineAdContent(
            title = "Ad Loading...",
            body = "",
            modifier = modifier,
        )

        is NativeAdState.Loaded -> AndroidView(
            modifier = modifier.fillMaxWidth(),
            factory = { context -> createNativeAdView(context, state.ad) },
        )

        NativeAdState.Failed -> {}
    }
}

@Preview
@Composable
private fun HilingualNativeLineAdPreview() {
    HilingualTheme {
        HilingualNativeLineAd("")
    }
}
