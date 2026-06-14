package com.hilingual.core.common.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import com.hilingual.core.common.trigger.LocalLoadErrorTrigger
import com.hilingual.core.common.trigger.LoadErrorHandleType

@Composable
fun HandleLoadError(
    uiState: UiState<*>,
    type: LoadErrorHandleType = LoadErrorHandleType.RETRY,
    onActionClick: () -> Unit,
) {
    val loadErrorTrigger = LocalLoadErrorTrigger.current
    val currentOnActionClick = rememberUpdatedState(onActionClick)

    LaunchedEffect(uiState, type) {
        when (uiState) {
            UiState.Failure -> {
                loadErrorTrigger.show(type = type) {
                    currentOnActionClick.value()
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
