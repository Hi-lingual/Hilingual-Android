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
package com.hilingual.core.ui.component.item.diary.toggle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hilingual.core.designsystem.component.button.HilingualSegmentedButton
import com.hilingual.core.designsystem.theme.HilingualTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun DiaryViewModeToggle(
    isShowCorrectedDiary: Boolean,
    onToggleViewMode: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    HilingualSegmentedButton(
        options = persistentListOf("교정본", "원본"),
        selectedIndex = if (isShowCorrectedDiary) 0 else 1,
        onSelect = { selectedIndex ->
            onToggleViewMode(selectedIndex == 0)
        },
        modifier = modifier,
    )
}

@Preview
@Composable
private fun DiaryViewModeTogglePreview() {
    HilingualTheme {
        var isShowCorrectedDiary by remember { mutableStateOf(true) }

        DiaryViewModeToggle(
            isShowCorrectedDiary = isShowCorrectedDiary,
            onToggleViewMode = { isShowCorrectedDiary = it },
        )
    }
}
