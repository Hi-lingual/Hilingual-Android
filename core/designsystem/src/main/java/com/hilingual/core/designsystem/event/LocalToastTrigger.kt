package com.hilingual.core.designsystem.event

import androidx.compose.runtime.staticCompositionLocalOf

val LocalToastTrigger = staticCompositionLocalOf<(String) -> Unit> {
    error("No Trigger provided")
}