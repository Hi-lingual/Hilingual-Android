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

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.R as DesignSystemR

enum class WordSortType(
    val text: String,
    @DrawableRes val iconRes: Int,
    val sortParam: Int
) {
    Latest(text = "최신순", iconRes = DesignSystemR.drawable.ic_listdown_24, sortParam = 1),
    AtoZ(text = "A-Z 순", iconRes = DesignSystemR.drawable.ic_az_24, sortParam = 2)
}

@Composable
internal fun VocaInfo(
    wordCount: Int,
    sortType: WordSortType,
    onSortClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "총 ${wordCount}개",
            style = HilingualTheme.typography.bodyM14,
            color = HilingualTheme.colors.gray500
        )
        Row(
            modifier = Modifier.noRippleClickable(onClick = onSortClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(DesignSystemR.drawable.ic_list_24),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Text(
                text = sortType.text,
                style = HilingualTheme.typography.bodyM14,
                color = HilingualTheme.colors.gray500
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VocaInfoPreview() {
    HilingualTheme {
        VocaInfo(wordCount = 98, sortType = WordSortType.Latest, onSortClick = {})
    }
}
