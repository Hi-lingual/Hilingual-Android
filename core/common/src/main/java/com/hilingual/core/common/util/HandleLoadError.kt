package com.hilingual.core.common.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import com.hilingual.core.common.model.LoadErrorActionType
import com.hilingual.core.common.model.LoadErrorHandleAction
import com.hilingual.core.common.trigger.LocalLoadErrorTrigger

@Composable
fun HandleLoadError(
    uiState: UiState<*>,
    defaultHandleAction: LoadErrorHandleAction = LoadErrorHandleAction.Common(LoadErrorActionType.RETRY),
    onActionClick: () -> Unit,
) {
    val loadErrorTrigger = LocalLoadErrorTrigger.current
    val currentOnActionClick = rememberUpdatedState(onActionClick)

    LaunchedEffect(uiState, defaultHandleAction) {
        when (uiState) {
            is UiState.Failure -> {
                val resolvedHandleAction = uiState.handleAction ?: defaultHandleAction

                loadErrorTrigger.show(handleAction = resolvedHandleAction) {
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
