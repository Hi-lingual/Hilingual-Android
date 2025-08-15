package com.hilingual.core.common.event

import androidx.compose.runtime.staticCompositionLocalOf

val LocalSnackbarTrigger = staticCompositionLocalOf<() -> Unit> {
    error("No SnackBar provided")
}
