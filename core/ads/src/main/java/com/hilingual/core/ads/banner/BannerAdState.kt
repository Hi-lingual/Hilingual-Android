package com.hilingual.core.ads.banner

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.libraries.ads.mobile.sdk.banner.AdView
import com.hilingual.core.ads.utils.screenWidthDp

@ConsistentCopyVisibility
@Stable
data class BannerAdHolder internal constructor(
    internal val adView: AdView,
    private val _isLoaded: State<Boolean>,
) {
    val isLoaded: Boolean get() = _isLoaded.value
}

@Composable
fun rememberBannerAdView(
    adUnitId: String,
    maxHeight: Int? = null,
): BannerAdHolder {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val screenWidth = context.screenWidthDp

    val isLoadedState = remember { mutableStateOf(false) }

    val adView = remember {
        createAndLoadAdView(
            context = context,
            activity = activity,
            adUnitId = adUnitId,
            screenWidth = screenWidth,
            maxHeight = maxHeight,
            onLoaded = { isLoadedState.value = true },
        )
    }

    DisposableEffect(adUnitId) {
        onDispose {
            adView.destroy()
        }
    }

    return remember { BannerAdHolder(adView, isLoadedState) }
}
