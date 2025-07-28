package com.hilingual.core.common.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

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
