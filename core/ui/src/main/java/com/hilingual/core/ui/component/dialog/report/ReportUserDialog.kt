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
package com.hilingual.core.ui.component.dialog.report

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.dialog.TwoButtonDialog
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun ReportUserDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onReportClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        TwoButtonDialog(
            modifier = modifier,
            title = "계정을 신고하시겠어요?",
            description = "신고된 계정은 확인 후\n" +
                "서비스의 운영원칙에 따라 처리돼요.",
            cancelText = "아니요",
            confirmText = "신고하기",
            onNegative = onDismiss,
            onPositive = onReportClick,
            onDismiss = onDismiss
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReportUserDialogPreview() {
    HilingualTheme {
        ReportUserDialog(
            isVisible = true,
            onDismiss = {},
            onReportClick = {}
        )
    }
}
