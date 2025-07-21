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
package com.hilingual.presentation.voca.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
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
internal fun AddVocaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = "일기 쓰고 단어 추가하기",
        style = HilingualTheme.typography.bodySB16,
        color = HilingualTheme.colors.white,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                shape = RoundedCornerShape(8.dp),
                color = HilingualTheme.colors.black
            )
            .width(IntrinsicSize.Max)
            .noRippleClickable(onClick = onClick)
            .padding(12.dp),
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
private fun AddVocaButtonPreview() {
    HilingualTheme {
        AddVocaButton(
            onClick = {}
        )
    }
}
