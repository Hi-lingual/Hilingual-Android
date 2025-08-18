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

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.R as DesignSystemR

@Composable
internal fun RecommendedTopicDropdown(
    enTopic: String,
    koTopic: String,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    var isKo by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .animateContentSize()
            .clip(RoundedCornerShape(8.dp))
            .background(HilingualTheme.colors.gray100)
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "오늘의 추천 주제 참고하기",
                style = HilingualTheme.typography.bodyM14,
                color = HilingualTheme.colors.gray700
            )
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer {
                        scaleY = if (isExpanded) -1f else 1f
                    }
                    .noRippleClickable {
                        isExpanded = !isExpanded
                        if (isExpanded) {
                            focusManager.clearFocus()
                        }
                    },
                imageVector = ImageVector.vectorResource(DesignSystemR.drawable.ic_arrow_down_20_gray),
                contentDescription = null,
                tint = HilingualTheme.colors.gray400
            )
        }

        if (isExpanded) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(HilingualTheme.colors.white)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = if (isKo) koTopic else enTopic,
                    style = HilingualTheme.typography.bodySB16,
                    color = HilingualTheme.colors.gray700,
                    maxLines = 2
                )
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .noRippleClickable { isKo = !isKo },
                    imageVector = ImageVector.vectorResource(DesignSystemR.drawable.ic_change_20),
                    contentDescription = null,
                    tint = HilingualTheme.colors.gray300
                )
            }
        }
    }
}

@Preview
@Composable
private fun RecommendedTopicDropdownPreview() {
    HilingualTheme {
        RecommendedTopicDropdown(
            enTopic = "What surprised you today?",
            koTopic = "오늘 무엇이 당신을 놀라게 했나요?",
            focusManager = LocalFocusManager.current
        )
    }
}
