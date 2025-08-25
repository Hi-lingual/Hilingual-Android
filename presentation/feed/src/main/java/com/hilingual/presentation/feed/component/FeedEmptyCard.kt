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
package com.hilingual.presentation.feed.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme

internal enum class FeedEmptyCardType(
    val text: String
) {
    NOT_FEED(
        text = "피드에 아직 공유된 일기가 없어요."
    ),
    NOT_FOLLOWING(
        text = "아직 팔로잉한 유저가 없어요.\n마음에 드는 사람을 찾아 팔로우해 보세요!"
    )
}

@Composable
internal fun FeedEmptyCard(
    type: FeedEmptyCardType,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)

    ) {
        Image(
            painter = painterResource(id = R.drawable.img_diary_empty),
            contentDescription = null,
            modifier = Modifier.size(width = 200.dp, height = 100.dp)
        )

        Text(
            text = type.text,
            style = HilingualTheme.typography.headM18,
            color = HilingualTheme.colors.gray500,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedEmptyCardPreview() {
    HilingualTheme {
        Column {
            FeedEmptyCard(
                FeedEmptyCardType.NOT_FEED
            )
            HorizontalDivider()
            FeedEmptyCard(
                FeedEmptyCardType.NOT_FOLLOWING
            )
        }
    }
}
