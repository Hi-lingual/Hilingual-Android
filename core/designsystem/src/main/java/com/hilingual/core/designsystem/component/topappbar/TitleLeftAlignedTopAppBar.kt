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
package com.hilingual.core.designsystem.component.topappbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun TitleLeftAlignedTopAppBar(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(start = 16.dp, top = 15.dp, end = 16.dp, bottom = 14.dp),
        textAlign = TextAlign.Start,
        text = title,
        color = HilingualTheme.colors.white,
        style = HilingualTheme.typography.headM18
    )
}

@Preview
@Composable
private fun TitleLeftAlignedTopAppBarPreview() {
    HilingualTheme {
        TitleLeftAlignedTopAppBar(
            title = "나의 단어장"
        )
    }
}
