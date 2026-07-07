package com.hilingual.core.common.model

sealed interface LoadErrorHandleAction {
    data object Retry : LoadErrorHandleAction

    data object Back : LoadErrorHandleAction

    data object NotFound : LoadErrorHandleAction
}
