package com.hilingual.core.common.trigger

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.SharedFlow

val LocalReconnectEvents = staticCompositionLocalOf<SharedFlow<Unit>> {
    error("No ReconnectEvents provided")
}
