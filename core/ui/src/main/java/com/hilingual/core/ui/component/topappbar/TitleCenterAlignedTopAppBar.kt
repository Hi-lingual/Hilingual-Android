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
package com.hilingual.core.ui.component.topappbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.topappbar.HilingualBasicTopAppBar
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
fun TitleCenterAlignedTopAppBar(
    modifier: Modifier = Modifier,
    title: String
) {
    HilingualBasicTopAppBar(
        modifier = modifier,
        title = title
    )
}

@Preview
@Composable
private fun TitleCenterAlignedTopAppBarPreview() {
    HilingualTheme {
        TitleCenterAlignedTopAppBar(
            title = "일기 작성하기"
        )
    }
}
