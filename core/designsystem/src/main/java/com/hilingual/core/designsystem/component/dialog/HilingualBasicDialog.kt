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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualBasicDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(
        usePlatformDefaultWidth = false,
        decorFitsSystemWindows = false
    ),
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = properties
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(HilingualTheme.colors.dim2)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = HilingualTheme.colors.white,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(top = 34.dp, start = 24.dp, end = 24.dp, bottom = 24.dp)
            ) {
                content()
            }
        }
    }
}

@Preview
@Composable
private fun HilingualBasicDialogPreview() {
    HilingualTheme {
        HilingualBasicDialog(
            onDismiss = {}
        ) {
            Text(
                text = "AI 피드백을 신고하시겠어요?",
                style = HilingualTheme.typography.headSB16,
                color = HilingualTheme.colors.gray850
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "신고된 AI 피드백은 확인 후\n서비스의 운영원칙에 따라 처리됩니다",
                style = HilingualTheme.typography.captionR12,
                color = HilingualTheme.colors.gray400,
                maxLines = 2,
                minLines = 2,
                textAlign = TextAlign.Center
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(13.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = HilingualTheme.colors.gray100)
                        .padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "취소",
                        style = HilingualTheme.typography.bodyM16,
                        color = HilingualTheme.colors.gray400
                    )
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(HilingualTheme.colors.hilingualOrange)
                        .padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "확인",
                        style = HilingualTheme.typography.bodyM16,
                        color = HilingualTheme.colors.white
                    )
                }
            }
        }
    }
}
