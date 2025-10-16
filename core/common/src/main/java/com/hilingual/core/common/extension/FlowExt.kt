/*
 * Copyright 2025 The Hilingual Project
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
package com.hilingual.core.common.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

@Composable
fun <T> Flow<T>.collectSideEffect(
    key: Any? = Unit,
    collector: suspend (T) -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(key) {
        flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collect(collector)
    }
}

@Composable
fun <T> Flow<T>.collectLatestSideEffect(
    key: Any? = Unit,
    collector: suspend (T) -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(key) {
        flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collectLatest(collector)
    }
}

fun <T> Flow<T>.pairwise(): Flow<Pair<T?, T>> = flow {
    var previous: T? = null
    collect { current ->
        emit(Pair(previous, current))
        previous = current
    }
}
