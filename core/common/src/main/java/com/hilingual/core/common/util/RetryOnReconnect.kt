/*
 * Copyright 2026 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
