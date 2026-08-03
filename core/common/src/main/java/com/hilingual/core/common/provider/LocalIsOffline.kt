package com.hilingual.core.common.provider

import androidx.compose.runtime.compositionLocalOf

val LocalIsOffline = compositionLocalOf<Boolean> {
    error("No isOffline provided")
}
