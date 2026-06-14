package com.hilingual.core.common.trigger

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember

@Immutable
data class LoadErrorState(
    val isVisible: Boolean = false,
    val onRetryClick: () -> Unit = {},
)

@Stable
class LoadErrorTrigger(
    private val onShow: (() -> Unit) -> Unit,
    private val onDismiss: () -> Unit,
) {
    fun show(onRetryClick: () -> Unit) {
        onShow(onRetryClick)
    }

    fun dismiss() {
        onDismiss()
    }
}

@Composable
fun rememberLoadErrorTrigger(
    show: (() -> Unit) -> Unit,
    dismiss: () -> Unit,
): LoadErrorTrigger = remember(show, dismiss) {
    LoadErrorTrigger(
        onShow = show,
        onDismiss = dismiss,
    )
}
