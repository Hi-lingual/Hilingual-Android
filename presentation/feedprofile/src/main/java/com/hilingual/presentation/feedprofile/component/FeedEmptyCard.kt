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
package com.hilingual.presentation.feedprofile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.theme.HilingualTheme

internal enum class FeedEmptyCardType(
    val text: String
) {
    NOT_SHARED(
        text = "아직 공유한 일기가 없어요."
    ),
    NOT_LIKED(
        text = "아직 공감한 일기가 없어요."
    ),
    NO_FOLLOWER(
        text = "아직 팔로워가 없어요."
    ),
    NO_FOLLOWING(
        text = "아직 팔로잉한 유저가 없어요."
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
            modifier = Modifier
                .size(width = 200.dp, height = 100.dp),
            painter = painterResource(id = R.drawable.img_diary_empty),
            contentDescription = null
        )
        Text(
            text = type.text,
            style = HilingualTheme.typography.headM18,
            color = HilingualTheme.colors.gray500,
            textAlign = TextAlign.Center
        )
    }
}

private class FeedEmptyCardPreviewParameterProvider :
    PreviewParameterProvider<FeedEmptyCardType> {
    override val values: Sequence<FeedEmptyCardType>
        get() = FeedEmptyCardType.entries.asSequence()
}

@Preview(showBackground = true)
@Composable
private fun FeedEmptyCardPreview(
    @PreviewParameter(FeedEmptyCardPreviewParameterProvider::class)
    type: FeedEmptyCardType
) {
    HilingualTheme {
        FeedEmptyCard(type = type)
    }
}
