package com.hilingual.core.common.extension

import com.hilingual.core.common.model.LoadErrorHandleAction
import retrofit2.HttpException

fun Throwable.isHttpNotFound(): Boolean = this is HttpException && code() == 404

fun Throwable.toLoadErrorHandleAction(): LoadErrorHandleAction? =
    if (isHttpNotFound()) LoadErrorHandleAction.NotFound else null
