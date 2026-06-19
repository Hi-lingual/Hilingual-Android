package com.hilingual.core.common.model

sealed class LoadErrorHandleAction {
    data class Common(
        val actionType: LoadErrorActionType,
    ) : LoadErrorHandleAction()

    data object NotFound : LoadErrorHandleAction()
}

enum class LoadErrorActionType {
    RETRY,
    BACK,
}
