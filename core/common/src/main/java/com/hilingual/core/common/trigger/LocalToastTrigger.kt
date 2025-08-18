package com.hilingual.core.common.trigger

import androidx.compose.runtime.staticCompositionLocalOf

val LocalToastTrigger = staticCompositionLocalOf<(message: String) -> Unit> {
    error("No Toast provided")
}
