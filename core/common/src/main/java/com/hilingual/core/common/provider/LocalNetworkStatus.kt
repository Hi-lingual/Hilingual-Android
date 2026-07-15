package com.hilingual.core.common.provider

import androidx.compose.runtime.compositionLocalOf

val LocalNetworkStatus = compositionLocalOf<Boolean> {
    error("No NetworkStatus provided")
}
