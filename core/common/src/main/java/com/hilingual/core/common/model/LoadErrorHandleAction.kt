package com.hilingual.core.common.model

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

fun LoadErrorHandleAction?.orRetry(): LoadErrorHandleAction = this ?: LoadErrorHandleAction.Retry

fun LoadErrorHandleAction?.orBack(): LoadErrorHandleAction = this ?: LoadErrorHandleAction.Back
