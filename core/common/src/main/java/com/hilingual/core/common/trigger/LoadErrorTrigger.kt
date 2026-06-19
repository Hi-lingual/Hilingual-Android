package com.hilingual.core.common.trigger

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.hilingual.core.common.model.LoadErrorActionType
import com.hilingual.core.common.model.LoadErrorHandleAction

@Immutable
data class LoadErrorState(
    val isVisible: Boolean = false,
    val handleAction: LoadErrorHandleAction = LoadErrorHandleAction.Common(LoadErrorActionType.RETRY),
    val onActionClick: () -> Unit = {},
)

@Stable
class LoadErrorTrigger(
    private val onShow: (LoadErrorHandleAction, () -> Unit) -> Unit,
    private val onDismiss: () -> Unit,
) {
    fun show(
        handleAction: LoadErrorHandleAction = LoadErrorHandleAction.Common(LoadErrorActionType.RETRY),
        onActionClick: () -> Unit,
    ) {
        onShow(handleAction, onActionClick)
    }

    fun dismiss() {
        onDismiss()
    }
}

@Composable
fun rememberLoadErrorTrigger(
    show: (LoadErrorHandleAction, () -> Unit) -> Unit,
    dismiss: () -> Unit,
): LoadErrorTrigger = remember(show, dismiss) {
    LoadErrorTrigger(
        onShow = show,
        onDismiss = dismiss,
    )
}
