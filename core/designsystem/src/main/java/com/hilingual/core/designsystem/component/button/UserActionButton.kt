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
package com.hilingual.core.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun UserActionButton(
    isFilled: Boolean,
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (buttonColor, textColor, borderColor) = if (isFilled) {
        Triple(
            HilingualTheme.colors.white,
            HilingualTheme.colors.gray500,
            HilingualTheme.colors.gray200
        )
    } else {
        Triple(
            HilingualTheme.colors.hilingualBlack,
            HilingualTheme.colors.white,
            HilingualTheme.colors.hilingualBlack
        )
    }

    Text(
        text = buttonText,
        color = textColor,
        style = HilingualTheme.typography.bodySB14,
        textAlign = TextAlign.Center,
        modifier = modifier
            .noRippleClickable(onClick = onClick)
            .width(80.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(buttonColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(vertical = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun UserActionButtonPreview() {
    HilingualTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            UserActionButton(
                isFilled = false,
                buttonText = "팔로우",
                onClick = {}
            )
            UserActionButton(
                isFilled = false,
                buttonText = "맞팔로우",
                onClick = {}
            )
            UserActionButton(
                isFilled = true,
                buttonText = "팔로잉",
                onClick = {}
            )
        }
    }
}
