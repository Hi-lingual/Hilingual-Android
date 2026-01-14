package com.hilingual.core.common.model

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Immutable

enum class MessageDuration(val millis: Long) {
    DEFAULT(3000L)
}

@Immutable
sealed interface HilingualMessage : SnackbarVisuals {
    val id: Long
    val messageDuration: MessageDuration

    override val duration: SnackbarDuration get() = SnackbarDuration.Indefinite
    override val withDismissAction: Boolean get() = false

    /**
     * [Toast] : 단순 정보 전달 (TextToast 사용)
     */
    data class Toast(
        override val message: String,
        override val messageDuration: MessageDuration = MessageDuration.DEFAULT
    ) : HilingualMessage {
        override val id: Long = System.nanoTime()
        override val actionLabel: String? = null
    }

    /**
     * [Snackbar] : 액션 버튼 포함 (HilingualActionSnackbar 사용)
     */
    data class Snackbar(
        override val message: String,
        val actionLabelText: String,
        val onAction: () -> Unit,
        override val messageDuration: MessageDuration = MessageDuration.DEFAULT
    ) : HilingualMessage {
        override val id: Long = System.nanoTime()
        override val actionLabel: String = actionLabelText
        override val withDismissAction: Boolean = true
    }
}
