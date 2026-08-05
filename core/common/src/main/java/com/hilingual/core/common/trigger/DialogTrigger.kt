/*
 * Copyright 2025 The Hilingual Project
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
package com.hilingual.core.common.trigger

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

enum class DialogType {
    ERROR,
    NOT_FOUND,
}

@Immutable
data class DialogState(
    val isVisible: Boolean = false,
    val type: DialogType = DialogType.ERROR,
    val onClickAction: () -> Unit = {},
)

@Stable
class DialogTrigger(
    private val onShow: (DialogType, () -> Unit) -> Unit,
) {
    fun show(
        type: DialogType = DialogType.ERROR,
        onClick: () -> Unit,
    ) {
        onShow(type, onClick)
    }
}

@Composable
fun rememberDialogTrigger(
    show: (DialogType, () -> Unit) -> Unit,
): DialogTrigger {
    val currentShow = rememberUpdatedState(show)

    return remember {
        DialogTrigger { type, onClick ->
            currentShow.value(type, onClick)
        }
    }
}
