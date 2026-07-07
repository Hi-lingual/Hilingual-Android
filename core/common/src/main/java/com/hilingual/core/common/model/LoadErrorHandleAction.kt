package com.hilingual.core.common.model

import androidx.compose.runtime.Stable

@Stable
sealed class LoadErrorHandleAction {
    data class Common(
        val actionType: LoadErrorActionType,
    ) : LoadErrorHandleAction()

    data object NotFound : LoadErrorHandleAction()

    companion object {
        val Retry = Common(LoadErrorActionType.RETRY)
        val Back = Common(LoadErrorActionType.BACK)
    }
}

enum class LoadErrorActionType {
    RETRY,
    BACK,
}
