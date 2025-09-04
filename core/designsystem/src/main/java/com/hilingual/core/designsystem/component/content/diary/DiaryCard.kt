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
package com.hilingual.core.designsystem.component.content.diary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.component.image.ErrorImageSize
import com.hilingual.core.designsystem.component.image.NetworkImage
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.SuitMedium
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun DiaryCard(
    isAIWritten: Boolean,
    diaryContent: String,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier,
    diffRanges: ImmutableList<Pair<Int, Int>> = persistentListOf(),
    imageUrl: String? = null
) {
    val maxContentLength = if (isAIWritten) 1500 else 1000

    val clipContent = diaryContent.run {
        if (length > maxContentLength) this.take(maxContentLength) else this
    }

    val displayText: AnnotatedString = if (isAIWritten) {
        getAnnotatedString(clipContent, diffRanges)
    } else {
        AnnotatedString(clipContent)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(HilingualTheme.colors.white)
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        if (imageUrl != null) {
            NetworkImage(
                imageUrl = imageUrl,
                shape = RoundedCornerShape(8.dp),
                errorImageSize = ErrorImageSize.LARGE,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f / 0.6f)
                    .noRippleClickable(
                        onClick = onImageClick
                    )
            )
        }

        Text(
            text = displayText,
            style = HilingualTheme.typography.bodyR16,
            color = HilingualTheme.colors.black,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "${clipContent.length}/$maxContentLength",
            style = HilingualTheme.typography.captionR12,
            color = HilingualTheme.colors.gray400,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun getAnnotatedString(
    content: String,
    diffRanges: ImmutableList<Pair<Int, Int>>
): AnnotatedString {
    return buildAnnotatedString {
        append(content)
        diffRanges.forEach {
            addStyle(
                style = SpanStyle(
                    color = HilingualTheme.colors.hilingualOrange,
                    fontFamily = SuitMedium
                ),
                start = it.first,
                end = it.second
            )
        }
    }
}

private data class DiaryCardPreviewState(
    val isAIDiary: Boolean,
    val imageUrl: String?,
    val content: String,
    val diffRanges: ImmutableList<Pair<Int, Int>>
)

private class DiaryContentCardPreviewProvider :
    PreviewParameterProvider<DiaryCardPreviewState> {
    override val values = sequenceOf(
        DiaryCardPreviewState(
            isAIDiary = false,
            imageUrl = "",
            content = "이미지 & 텍스트",
            diffRanges = persistentListOf()
        ),
        DiaryCardPreviewState(
            isAIDiary = false,
            imageUrl = null,
            content = "I want to become a teacher future. Because I like child.",
            diffRanges = persistentListOf()
        ),
        DiaryCardPreviewState(
            isAIDiary = true,
            imageUrl = null,
            content = "Today I went to the cafe Conhas in Yeonnam to meet my teammates.\n I was planning to arrive around 1:30 p.m., but I got there at 2:20 because I overslept, as always.\n I wore rain boots and brought my favorite umbrella because the weather forecast said it would rain all day, but it wasn’t really raining much outside.\n I got kind of disappointed. But yes, no rain is better than rain, I guess.\n" +
                "After arriving, I had a jambon arugula sandwich with a vanilla latte.\n Honestly, I should be more careful when I'm drinking milk because I get stomachaches easily, but I always order lattes.\nMy life feels like a disaster, a mess that I call myself.\n But they tasted really good, so I felt more motivated to work.\n I really liked this café because it's spacious, chill, and has a great atmosphere for focusing.\n I’ll definitely come back again soon!",
            diffRanges = persistentListOf(
                Pair(84, 164),
                Pair(278, 316),
                Pair(508, 583),
                Pair(740, 802)
            )
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
private fun DiaryCardPreview(
    @PreviewParameter(DiaryContentCardPreviewProvider::class) state: DiaryCardPreviewState
) {
    HilingualTheme {
        DiaryCard(
            isAIWritten = state.isAIDiary,
            diaryContent = state.content,
            imageUrl = state.imageUrl,
            diffRanges = state.diffRanges,
            onImageClick = {}
        )
    }
}
