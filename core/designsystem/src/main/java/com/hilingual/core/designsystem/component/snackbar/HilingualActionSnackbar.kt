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
package com.hilingual.core.designsystem.component.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.common.model.HilingualMessage
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualActionSnackbar(
    message: HilingualMessage.Snackbar,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(size = 8.dp))
            .background(color = HilingualTheme.colors.gray500)
            .padding(vertical = 10.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = message.message,
            style = HilingualTheme.typography.bodyR16,
            color = HilingualTheme.colors.white
        )

        Text(
            text = message.actionLabelText,
            style = HilingualTheme.typography.bodyM14,
            color = HilingualTheme.colors.white,
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(HilingualTheme.colors.gray700)
                .noRippleClickable(
                    onClick = {
                        message.onAction()
                        onDismiss()
                    }
                )
                .padding(vertical = 7.dp, horizontal = 11.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiarySnackbarPreview() {
    HilingualTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HilingualActionSnackbar(
                message = HilingualMessage.Snackbar(
                    message = "일기가 게시되었어요!",
                    actionLabelText = "보러가기",
                    onAction = {}
                ),
                onDismiss = {},
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 23.dp)
            )
        }
    }
}
