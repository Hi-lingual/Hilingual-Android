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
package com.hilingual.presentation.diarywrite.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.diarywrite.R

@Composable
internal fun WriteGuideTooltip(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = modifier
                .clip(shape = RoundedCornerShape(8.dp))
                .background(HilingualTheme.colors.hilingualBlack)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            text = text,
            style = HilingualTheme.typography.bodyM16,
            color = HilingualTheme.colors.white
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_tooltip_arrow),
            contentDescription = null,
            tint = HilingualTheme.colors.hilingualBlack
        )
    }
}

@Preview
@Composable
private fun WriteGuideTooltipPreview() {
    HilingualTheme {
        WriteGuideTooltip(
            text = "10자 이상 작성해야 피드백 요청이 가능해요!"
        )
    }
}
