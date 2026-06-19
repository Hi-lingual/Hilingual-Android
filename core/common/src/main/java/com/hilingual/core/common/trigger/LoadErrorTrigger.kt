package com.hilingual.core.common.trigger

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.hilingual.core.common.model.LoadErrorHandleType

@Immutable
data class LoadErrorState(
    val isVisible: Boolean = false,
    val type: LoadErrorHandleType = LoadErrorHandleType.RETRY,
    val onActionClick: () -> Unit = {},
)

@Stable
class LoadErrorTrigger(
    private val onShow: (LoadErrorHandleType, () -> Unit) -> Unit,
    private val onDismiss: () -> Unit,
) {
    fun show(
        type: LoadErrorHandleType = LoadErrorHandleType.RETRY,
        onActionClick: () -> Unit,
    ) {
        onShow(type, onActionClick)
    }

    fun dismiss() {
        onDismiss()
    }
}

@Composable
fun rememberLoadErrorTrigger(
    show: (LoadErrorHandleType, () -> Unit) -> Unit,
    dismiss: () -> Unit,
): LoadErrorTrigger = remember(show, dismiss) {
    LoadErrorTrigger(
        onShow = show,
        onDismiss = dismiss,
    )
}
