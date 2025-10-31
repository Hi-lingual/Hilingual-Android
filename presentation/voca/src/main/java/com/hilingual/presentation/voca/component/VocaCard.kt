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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.ui.component.item.voca.WordPhraseTypeTag
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.hilingual.core.designsystem.R as DesignSystemR

@Composable
internal fun VocaCard(
    phrase: String,
    phraseType: ImmutableList<String>,
    onCardClick: () -> Unit,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .noRippleClickable(onClick = onCardClick)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                color = HilingualTheme.colors.white,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)

            ) {
                phraseType.forEach { type ->
                    key(type) {
                        WordPhraseTypeTag(phraseType = type)
                    }
                }
            }
            Text(
                text = phrase,
                style = HilingualTheme.typography.headM18,
                color = HilingualTheme.colors.black
            )
        }
        Icon(
            imageVector = ImageVector.vectorResource(
                id = if (isBookmarked) {
                    DesignSystemR.drawable.ic_save_28_filled
                } else {
                    DesignSystemR.drawable.ic_save_28_empty
                }
            ),
            contentDescription = null,
            modifier = Modifier.noRippleClickable(onClick = onBookmarkClick),
            tint = Color.Unspecified
        )
    }
}

@Preview
@Composable
private fun VocaCardPreview() {
    HilingualTheme {
        var isBookmarked by remember { mutableStateOf(true) }
        VocaCard(
            phrase = "run late",
            phraseType = persistentListOf("동사", "숙어"),
            onCardClick = {},
            isBookmarked = isBookmarked,
            onBookmarkClick = { isBookmarked = !isBookmarked }
        )
    }
}
