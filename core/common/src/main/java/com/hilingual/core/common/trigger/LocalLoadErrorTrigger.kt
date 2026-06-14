package com.hilingual.core.common.trigger

import androidx.compose.runtime.staticCompositionLocalOf

val LocalLoadErrorTrigger = staticCompositionLocalOf<LoadErrorTrigger> {
    error("No LoadErrorTrigger provided")
}
