package com.hilingual.core.common.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import com.hilingual.core.common.trigger.LocalReconnectEvents

@Composable
fun RetryOnReconnect(
    isLoading: Boolean,
    shouldRetry: Boolean,
    onRetry: () -> Unit,
) {
    val reconnectEvents = LocalReconnectEvents.current
    val currentIsLoading by rememberUpdatedState(isLoading)
    val currentShouldRetry by rememberUpdatedState(shouldRetry)
    val currentOnRetry by rememberUpdatedState(onRetry)
    var retryOnCurrentLoadFailure by remember { mutableStateOf(false) }

    LaunchedEffect(reconnectEvents) {
        reconnectEvents.collect {
            when {
                currentShouldRetry -> {
                    retryOnCurrentLoadFailure = false
                    currentOnRetry()
                }

                currentIsLoading -> retryOnCurrentLoadFailure = true

                else -> retryOnCurrentLoadFailure = false
            }
        }
    }

    LaunchedEffect(isLoading, shouldRetry, retryOnCurrentLoadFailure) {
        if (!retryOnCurrentLoadFailure || isLoading) return@LaunchedEffect

        retryOnCurrentLoadFailure = false
        if (shouldRetry) {
            currentOnRetry()
        }
    }
}
