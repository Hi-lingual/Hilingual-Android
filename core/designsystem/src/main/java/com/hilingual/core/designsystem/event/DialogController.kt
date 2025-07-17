package com.hilingual.core.designsystem.event

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Stable
class DialogController {
    var isVisible by mutableStateOf(false)
        private set

    var onClickAction by mutableStateOf<() -> Unit>({})
        private set

    fun show(onClick: () -> Unit) {
        onClickAction = onClick
        isVisible = true
    }

    fun dismiss() {
        isVisible = false
    }
}

@Composable
fun rememberDialogController(): DialogController = remember {
    DialogController()
}
