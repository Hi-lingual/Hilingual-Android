/*
 * Copyright 2026 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
