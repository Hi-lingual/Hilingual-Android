package com.hilingual.core.common.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import com.hilingual.core.common.trigger.LocalLoadErrorTrigger

@Composable
fun HandleLoadError(
    uiState: UiState<*>,
    onRetryClick: () -> Unit,
) {
    val loadErrorTrigger = LocalLoadErrorTrigger.current
    val currentOnRetryClick = rememberUpdatedState(onRetryClick)

    LaunchedEffect(uiState) {
        when (uiState) {
            UiState.Failure -> {
                loadErrorTrigger.show {
                    currentOnRetryClick.value()
                }
            }

            UiState.Empty,
            UiState.Loading,
            is UiState.Success -> {
                loadErrorTrigger.dismiss()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            loadErrorTrigger.dismiss()
        }
    }
}
