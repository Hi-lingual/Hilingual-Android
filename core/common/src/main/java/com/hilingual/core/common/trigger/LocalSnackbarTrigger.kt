package com.hilingual.core.common.trigger

import androidx.compose.runtime.staticCompositionLocalOf
import com.hilingual.core.common.model.SnackbarRequest

val LocalSnackbarTrigger = staticCompositionLocalOf<(SnackbarRequest) -> Unit> {
    error("No SnackBar provided")
}
