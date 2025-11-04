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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.component.toggle.HilingualBasicToggleSwitch
import com.hilingual.core.designsystem.theme.HilingualTheme

@Composable
internal fun DiaryViewModeToggle(
    isAIWritten: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(
            text = "교정된 일기",
            style = HilingualTheme.typography.captionR14,
            color = HilingualTheme.colors.gray500
        )
        HilingualBasicToggleSwitch(
            isChecked = isAIWritten,
            onCheckedChange = onToggle
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AIDiaryTogglePreview() {
    HilingualTheme {
        var isAI by remember { mutableStateOf(true) }

        DiaryViewModeToggle(
            isAIWritten = isAI,
            onToggle = { isAI = it }
        )
    }
}
