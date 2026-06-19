package com.hilingual.core.common.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import com.hilingual.core.common.model.LoadErrorHandleType
import com.hilingual.core.common.trigger.LocalLoadErrorTrigger

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
            is UiState.Failure -> {
                loadErrorTrigger.show(type = uiState.errorType ?: type) {
                    currentOnActionClick.value()
                }
            }

            UiState.Empty,
            UiState.Loading,
            is UiState.Success,
            -> {
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
