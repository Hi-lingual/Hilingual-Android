package com.hilingual.core.common.extension

import com.hilingual.core.common.model.LoadErrorHandleType
import retrofit2.HttpException

fun Throwable.isHttpNotFound(): Boolean = this is HttpException && code() == 404

fun Throwable.toLoadErrorHandleType(): LoadErrorHandleType =
    if (isHttpNotFound()) LoadErrorHandleType.NOT_FOUND else LoadErrorHandleType.RETRY
