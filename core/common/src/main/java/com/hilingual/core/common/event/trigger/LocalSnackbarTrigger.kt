package com.hilingual.core.common.event.trigger

import androidx.compose.runtime.staticCompositionLocalOf

val LocalSnackbarTrigger = staticCompositionLocalOf<() -> Unit> {
    error("No SnackBar provided")
}
