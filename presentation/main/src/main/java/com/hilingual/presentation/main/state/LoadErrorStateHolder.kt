package com.hilingual.presentation.main.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.hilingual.core.common.trigger.LoadErrorState

@Stable
internal class LoadErrorStateHolder {
    var loadErrorState by mutableStateOf(LoadErrorState())
        private set

    fun showLoadError(onRetryClick: () -> Unit) {
        loadErrorState = LoadErrorState(
            isVisible = true,
            onRetryClick = onRetryClick,
        )
    }

    fun dismissLoadError() {
        loadErrorState = LoadErrorState()
    }

    fun retry() {
        val retryAction = loadErrorState.onRetryClick
        dismissLoadError()
        retryAction()
    }
}

@Composable
internal fun rememberLoadErrorStateHolder(): LoadErrorStateHolder = remember {
    LoadErrorStateHolder()
}
