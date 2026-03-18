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
    val isPreviewMode = LocalInspectionMode.current

    if (isPreviewMode) {
        NativeLineAdContent(
            title = "광고 이름",
            body = "메인 카피",
            modifier = modifier,
        )
    } else {
        val nativeAd = rememberNativeAd(adUnitId)
        if (nativeAd != null) {
            AndroidView(
                modifier = modifier.fillMaxWidth(),
                factory = { context ->
                    createNativeAdView(context, nativeAd)
                },
            )
        }
    }
}

@Preview
@Composable
private fun HilingualNativeLineAdPreview() {
    HilingualTheme {
        HilingualNativeLineAd("")
    }
}
