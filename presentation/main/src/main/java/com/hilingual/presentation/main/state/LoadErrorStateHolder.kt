package com.hilingual.presentation.main.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.hilingual.core.common.trigger.LoadErrorState
import com.hilingual.core.common.trigger.LoadErrorHandleType

@Stable
internal class LoadErrorStateHolder {
    var loadErrorState by mutableStateOf(LoadErrorState())
        private set

    fun showLoadError(
        type: LoadErrorHandleType,
        onActionClick: () -> Unit,
    ) {
        loadErrorState = LoadErrorState(
            isVisible = true,
            type = type,
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
