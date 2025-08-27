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
package com.hilingual.core.designsystem.component.bottomsheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportBlockBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onReportClick: () -> Unit,
    onBlockClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HilingualMenuBottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        HilingualMenuBottomSheetItem(
            text = "계정 차단하기",
            iconResId = R.drawable.ic_block_24_gray,
            onClick = onBlockClick
        )
        HilingualMenuBottomSheetItem(
            text = "게시글 신고하기",
            iconResId = R.drawable.ic_report_24,
            onClick = onReportClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReportBlockBottomSheetPreviewVisible() {
    HilingualTheme {
        var isSheetVisible by remember { mutableStateOf(true) }

        ReportBlockBottomSheet(
            isVisible = isSheetVisible,
            onDismiss = { isSheetVisible = false },
            onReportClick = { },
            onBlockClick = { }
        )
    }
}
