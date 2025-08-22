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
package com.hilingual.core.designsystem.component.dialog.diary

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.dialog.TwoButtonDialog
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun DiaryPublishDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onPostClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        TwoButtonDialog(
            modifier = modifier,
            title = "영어 일기를 게시하시겠어요?",
            description = "공유된 일기는 모든 유저에게 게시되며, \n" +
                "피드에서 확인하실 수 있어요.",
            cancelText = "아니요",
            confirmText = "게시하기",
            onNegative = onDismiss,
            onPositive = onPostClick,
            onDismiss = onDismiss
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiaryPublishPreview() {
    HilingualTheme {
        DiaryPublishDialog(
            isVisible = true,
            onDismiss = {},
            onPostClick = { }
        )
    }
}
