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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun HilingualBasicTopAppBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable () -> Unit = {},
    backgroundColor: Color = HilingualTheme.colors.white
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // 좌측 아이콘
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            navigationIcon()
        }

        // title
        if (title != null) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = title,
                color = HilingualTheme.colors.black,
                style = HilingualTheme.typography.headB18
            )
        }

        // 우측 아이콘
        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            actions()
        }
    }
}

@Preview
@Composable
private fun HilingualBasicTopAppBarPreview() {
    HilingualTheme {
        HilingualBasicTopAppBar()
    }
}
