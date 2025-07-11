package com.hilingual.core.common.provider

import androidx.compose.runtime.staticCompositionLocalOf

val LocalSnackBarTrigger = staticCompositionLocalOf<(String) -> Unit> {
    error("No SnackBar provided")
}
