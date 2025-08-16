package com.hilingual.core.common.event.trigger

import androidx.compose.runtime.staticCompositionLocalOf

val LocalToastTrigger = staticCompositionLocalOf<(message: String) -> Unit> {
    error("No Toast provided")
}
