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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.event.DialogController
import com.hilingual.core.designsystem.event.rememberDialogController
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualErrorDialog(
    controller: DialogController,
    modifier: Modifier = Modifier
) {
    if (controller.isVisible) {
        OneButtonDialog(
            confirmText = "확인",
            onConfirm = {
                controller.onClickAction()
                controller.dismiss()
            },
            onDismiss = controller::dismiss,
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            ),
            modifier = modifier
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.img_error),
                    contentDescription = null,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "앗! 일시적인 오류가 발생했어요.",
                    style = HilingualTheme.typography.headB16,
                    color = HilingualTheme.colors.gray850
                )
            }
        }
    }
}

@Preview
@Composable
private fun HilingualErrorDialogPreview() {
    val controller = rememberDialogController()
    controller.show { }

    HilingualTheme {
        HilingualErrorDialog(
            controller = controller
        )
    }
}
