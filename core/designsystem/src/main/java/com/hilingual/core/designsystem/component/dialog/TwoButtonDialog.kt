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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.hilingual.core.designsystem.component.button.DialogButton
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun TwoButtonDialog(
    title: String,
    cancelText: String,
    confirmText: String,
    onNegative: () -> Unit,
    onPositive: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    properties: DialogProperties = DialogProperties(
        usePlatformDefaultWidth = false,
        decorFitsSystemWindows = false
    )
) {
    HilingualBasicDialog(
        onDismiss = onDismiss,
        modifier = modifier,
        properties = properties
    ) {
        Text(
            text = title,
            style = HilingualTheme.typography.headSB16,
            color = HilingualTheme.colors.gray850
        )

        if (description != null) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                style = HilingualTheme.typography.bodyR14,
                color = HilingualTheme.colors.gray500,
                maxLines = 2,
                textAlign = TextAlign.Center
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(13.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        ) {
            DialogButton(
                text = cancelText,
                onClick = onNegative,
                isFilled = false,
                modifier = Modifier.weight(1f)
            )
            DialogButton(
                text = confirmText,
                onClick = onPositive,
                isFilled = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TwoButtonDialogPreview() {
    HilingualTheme {
        TwoButtonDialog(
            title = "AI 피드백을 신고하시겠어요?",
            description = "신고된 AI 피드백은 확인 후\n서비스의 운영원칙에 따라 처리됩니다",
            cancelText = "취소",
            confirmText = "확인",
            onNegative = {},
            onPositive = {},
            onDismiss = {}
        )
    }
}
