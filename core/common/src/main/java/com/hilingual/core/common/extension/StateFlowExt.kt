package com.hilingual.core.common.extension

import com.hilingual.core.common.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

inline fun <T> MutableStateFlow<UiState<T>>.updateSuccess(
    crossinline onUpdate: (T) -> T
) {
    update { currentState ->
        if (currentState is UiState.Success) {
            currentState.copy(data = onUpdate(currentState.data))
        } else {
            currentState
        }
    }
}
