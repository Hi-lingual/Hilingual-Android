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
package com.hilingual.core.designsystem.component.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.hilingual.core.common.analytics.Page
import com.hilingual.core.common.analytics.TriggerType
import com.hilingual.core.common.provider.LocalTracker
import com.hilingual.core.common.trigger.DialogState
import com.hilingual.core.common.trigger.DialogType
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme

/**
 * @param page 이벤트 수집 대상 화면. null이면 이벤트를 수집하지 않는다.
 */
@Composable
fun HilingualErrorDialog(
    state: DialogState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    page: Page? = null,
) {
    val tracker = LocalTracker.current

    if (state.isVisible) {
        OneButtonDialog(
            confirmText = "확인",
            onConfirm = {
                page?.let {
                    tracker.logPageAction(
                        trigger = TriggerType.CLICK,
                        page = it,
                        action = state.type.eventName,
                    )
                }
                state.onClickAction()
                onDismiss()
            },
            onDismiss = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false,
            ),
            modifier = modifier,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(R.drawable.img_error),
                    contentDescription = null,
                    modifier = Modifier.padding(bottom = 8.dp),
                )

                Text(
                    text = state.type.message,
                    style = HilingualTheme.typography.headSB16,
                    color = HilingualTheme.colors.gray850,
                )
            }
        }
    }
}

private val DialogType.message: String
    get() = when (this) {
        DialogType.ERROR -> "앗! 일시적인 오류가 발생했어요."
        DialogType.NOT_FOUND -> "앗! 요청한 내용을 찾을 수 없어요"
    }

private val DialogType.eventName: String
    get() = when (this) {
        DialogType.ERROR -> "server_error_confirm"
        DialogType.NOT_FOUND -> "empty_data_confirm"
    }

@Preview
@Composable
private fun HilingualErrorDialogPreview() {
    val state = DialogState(isVisible = true)

    HilingualTheme {
        HilingualErrorDialog(
            state = state,
            onDismiss = {},
        )
    }
}
