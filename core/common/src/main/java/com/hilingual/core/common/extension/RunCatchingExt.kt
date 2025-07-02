package com.hilingual.core.common.extension

import timber.log.Timber

suspend inline fun <T> Result<T>.onLogFailure(
    crossinline action: suspend (exception: Throwable) -> Unit
): Result<T> = onFailure { e ->
    Timber.e(e)
    action(e)
}
