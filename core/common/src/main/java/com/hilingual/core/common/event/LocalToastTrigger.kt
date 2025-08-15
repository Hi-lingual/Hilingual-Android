package com.hilingual.core.common.event

import androidx.compose.runtime.staticCompositionLocalOf

val LocalToastTrigger = staticCompositionLocalOf<(message: String) -> Unit> {
    error("No Toast provided")
}
