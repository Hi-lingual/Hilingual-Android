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
package com.hilingual.core.ui.component.dialog.diary

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.dialog.TwoButtonDialog
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun DiaryUnpublishDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onPrivateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        TwoButtonDialog(
            modifier = modifier,
            title = "영어 일기를 비공개 하시겠어요?",
            description = "비공개로 전환 시, \n" +
                "해당 일기의 피드 활동 내역은 모두 사라져요.",
            cancelText = "아니요",
            confirmText = "비공개하기",
            onNegative = onDismiss,
            onPositive = onPrivateClick,
            onDismiss = onDismiss
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryUnpublishPreview() {
    HilingualTheme {
        DiaryUnpublishDialog(
            isVisible = true,
            onDismiss = {},
            onPrivateClick = { }
        )
    }
}
