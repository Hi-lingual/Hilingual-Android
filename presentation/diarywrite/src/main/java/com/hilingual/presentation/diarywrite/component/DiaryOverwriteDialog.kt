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
package com.hilingual.presentation.diarywrite.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.dialog.TwoButtonDialog
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun DiaryOverwriteDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onNoClick: () -> Unit,
    onOverwriteClick: () -> Unit
) {
    if (isVisible) {
        TwoButtonDialog(
            title = "이미 임시저장한 일기가 있어요.",
            description = "일자 당 하나의 일기만 임시저장할 수 있어요.\n임시저장한 일기에 덮어쓰시겠어요?",
            cancelText = "아니요",
            confirmText = "덮어쓰기",
            onNegative = onNoClick,
            onPositive = onOverwriteClick,
            onDismiss = onDismiss
        )
    }
}

@Preview
@Composable
private fun DiaryOverwriteDialogPreview() {
    HilingualTheme {
        DiaryOverwriteDialog(
            isVisible = true,
            onDismiss = {},
            onNoClick = {},
            onOverwriteClick = {}
        )
    }
}
