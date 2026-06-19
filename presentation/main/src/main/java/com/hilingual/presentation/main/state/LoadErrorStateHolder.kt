package com.hilingual.presentation.main.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.hilingual.core.common.model.LoadErrorHandleAction
import com.hilingual.core.common.trigger.LoadErrorState

@Stable
internal class LoadErrorStateHolder {
    var loadErrorState by mutableStateOf(LoadErrorState())
        private set

    fun showLoadError(
        handleAction: LoadErrorHandleAction,
        onActionClick: () -> Unit,
    ) {
        loadErrorState = LoadErrorState(
            isVisible = true,
            handleAction = handleAction,
            onActionClick = onActionClick,
        )
    }

    fun dismissLoadError() {
        loadErrorState = LoadErrorState()
    }

    fun performAction() {
        val action = loadErrorState.onActionClick
        dismissLoadError()
        action()
    }
}

@Composable
internal fun rememberLoadErrorStateHolder(): LoadErrorStateHolder = remember {
    LoadErrorStateHolder()
}
