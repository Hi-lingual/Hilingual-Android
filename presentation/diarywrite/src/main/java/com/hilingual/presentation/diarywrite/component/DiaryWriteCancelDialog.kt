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
internal fun DiaryWriteCancelDialog(
    onDismiss: () -> Unit,
    onNoClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    TwoButtonDialog(
        title = "일기 작성을 취소하시겠어요?",
        description = "지금 나가면 작성한 내용이 모두 사라져요!",
        cancelText = "아니요",
        confirmText = "취소하기",
        onNegative = onNoClick,
        onPositive = onCancelClick,
        onDismiss = onDismiss
    )
}

@Preview
@Composable
private fun DiaryWriteCancelDialogPreview() {
    HilingualTheme {
        DiaryWriteCancelDialog(
            onDismiss = {},
            onNoClick = {},
            onCancelClick = {}
        )
    }
}
