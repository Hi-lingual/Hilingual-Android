package com.hilingual.core.common.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import com.hilingual.core.common.trigger.LocalReconnectEvents

@Composable
fun RetryOnReconnect(
    shouldRetry: Boolean,
    onRetry: () -> Unit,
) {
    val reconnectEvents = LocalReconnectEvents.current
    val currentShouldRetry by rememberUpdatedState(shouldRetry)
    val currentOnRetry by rememberUpdatedState(onRetry)

    LaunchedEffect(reconnectEvents) {
        reconnectEvents.collect {
            if (currentShouldRetry) {
                currentOnRetry()
            }
        }
    }
}
