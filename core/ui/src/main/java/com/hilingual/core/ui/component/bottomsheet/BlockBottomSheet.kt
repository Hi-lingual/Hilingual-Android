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
package com.hilingual.core.ui.component.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.component.bottomsheet.HilingualBasicBottomSheet
import com.hilingual.core.designsystem.component.button.HilingualButton
import com.hilingual.core.designsystem.theme.HilingualTheme
import kotlinx.collections.immutable.persistentListOf

private val CHECK_TEXTS = persistentListOf(
    "상대의 모든 활동을 확인할 수 없어요.",
    "상대는 나의 모든 활동을 확인할 수 없어요."
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onBlockButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HilingualBasicBottomSheet(
        isVisible = isVisible,
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 12.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "정말 차단하실건가요?",
                style = HilingualTheme.typography.headB18,
                color = HilingualTheme.colors.black
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "차단 시 상대방은 차단 여부를 알 수 없으며,\n" +
                    "언제든 차단을 해제 할 수 있어요.",
                style = HilingualTheme.typography.bodyM14,
                color = HilingualTheme.colors.gray400
            )

            Spacer(Modifier.height(20.dp))

            CHECK_TEXTS.forEach { text ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_check_24),
                        contentDescription = null,
                        tint = HilingualTheme.colors.gray850,
                        modifier = Modifier.size(20.dp)
                    )

                    Text(
                        text = text,
                        style = HilingualTheme.typography.bodyM14,
                        color = HilingualTheme.colors.gray850
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            HilingualButton(
                text = "차단하기",
                onClick = onBlockButtonClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BlockBottomSheetPreviewVisible() {
    HilingualTheme {
        var isSheetVisible by remember { mutableStateOf(true) }

        BlockBottomSheet(
            isVisible = isSheetVisible,
            onDismiss = { isSheetVisible = false },
            onBlockButtonClick = {
                println("Block Button Clicked in Preview")
                isSheetVisible = false
            }
        )
    }
}
