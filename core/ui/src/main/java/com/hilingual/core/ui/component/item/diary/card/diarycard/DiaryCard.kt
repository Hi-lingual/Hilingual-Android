/*
 * Copyright 2026 The Hilingual Project
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
package com.hilingual.core.ui.component.item.diary.card.diarycard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.hilingual.core.common.extension.noRippleClickable
import com.hilingual.core.designsystem.R
import com.hilingual.core.designsystem.component.image.ErrorImageSize
import com.hilingual.core.designsystem.component.image.NetworkImage
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.core.designsystem.theme.PretendardMedium
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val MAX_CORRECTED = 1500
private const val MAX_ORIGINAL = 1000

@Composable
internal fun DiaryCard(
    isShowCorrectedDiary: Boolean,
    diaryContent: String,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier,
    diffRanges: ImmutableList<Pair<Int, Int>> = persistentListOf(),
    imageUrl: String? = null,
) {
    val maxContentLength = if (isShowCorrectedDiary) MAX_CORRECTED else MAX_ORIGINAL
    val content = diaryContent.take(maxContentLength)

    val ttsController = rememberTtsController()
    val textLayoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    LaunchedEffect(isShowCorrectedDiary) {
        if (!isShowCorrectedDiary) ttsController.stop()
    }

    val displayText = if (isShowCorrectedDiary) {
        buildDiaryAnnotatedString(
            content = content,
            diffRanges = diffRanges,
            diffColor = HilingualTheme.colors.hilingualOrange,
            unspokenColor = HilingualTheme.colors.gray300,
            spokenUpTo = ttsController.spokenUpTo,
        )
    } else {
        AnnotatedString(content)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(HilingualTheme.colors.white)
            .fillMaxWidth()
            .padding(12.dp),
    ) {
        if (imageUrl != null) {
            NetworkImage(
                imageUrl = imageUrl,
                shape = RoundedCornerShape(8.dp),
                errorImageSize = ErrorImageSize.LARGE,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f / 0.6f)
                    .noRippleClickable(onClick = onImageClick),
            )
        }

        Text(
            text = displayText,
            style = HilingualTheme.typography.bodyR15,
            color = HilingualTheme.colors.black,
            onTextLayout = { textLayoutResult.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isShowCorrectedDiary) {
                        Modifier.pointerInput(content) {
                            detectTapGestures { tapOffset ->
                                val charOffset = textLayoutResult.value
                                    ?.getOffsetForPosition(tapOffset)
                                    ?: return@detectTapGestures
                                ttsController.playFrom(findSentenceStart(content, charOffset), content)
                            }
                        }
                    } else {
                        Modifier
                    },
                ),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            if (isShowCorrectedDiary) {
                TtsPlayButton(
                    isPlaying = ttsController.isPlaying,
                    onClick = { ttsController.toggle(content) },
                )
            }
            Text(
                text = "${content.length}/$maxContentLength",
                style = HilingualTheme.typography.captionR12,
                color = HilingualTheme.colors.gray400,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun TtsPlayButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
) {
    val icon = if (isPlaying) R.drawable.ic_pause_20_and else R.drawable.ic_play_20_and
    val backgroundColor = if (isPlaying) HilingualTheme.colors.white else HilingualTheme.colors.hilingualBlue50
    val borderModifier = if (isPlaying) {
        Modifier.border(1.dp, HilingualTheme.colors.hilingualBlue, RoundedCornerShape(12.dp))
    } else {
        Modifier
    }
    Icon(
        imageVector = ImageVector.vectorResource(icon),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = Modifier
            .noRippleClickable(onClick = onClick)
            .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
            .then(borderModifier)
            .padding(horizontal = 12.dp, vertical = 4.dp),
    )
}

private fun findSentenceStart(text: String, offset: Int): Int {
    for (i in offset.coerceAtMost(text.lastIndex) downTo 0) {
        if (text[i] == '.' || text[i] == '!' || text[i] == '?' || text[i] == '\n') {
            return (i + 1 until text.length).firstOrNull { !text[it].isWhitespace() } ?: 0
        }
    }
    return 0
}

private fun Pair<Int, Int>.isValid(textLength: Int): Boolean {
    val (start, end) = this
    return start in 0 until textLength && end <= textLength && start < end
}

private fun buildDiaryAnnotatedString(
    content: String,
    diffRanges: ImmutableList<Pair<Int, Int>>,
    diffColor: Color,
    unspokenColor: Color,
    spokenUpTo: Int,
): AnnotatedString = buildAnnotatedString {
    append(content)
    diffRanges
        .filter { it.isValid(content.length) }
        .forEach { (start, end) ->
            addStyle(SpanStyle(color = diffColor, fontFamily = PretendardMedium), start, end)
        }
    if (spokenUpTo in 1 until content.length) {
        addStyle(SpanStyle(color = unspokenColor), spokenUpTo, content.length)
    }
}

private data class DiaryCardPreviewState(
    val isShowCorrectedDiary: Boolean,
    val imageUrl: String?,
    val content: String,
    val diffRanges: ImmutableList<Pair<Int, Int>>,
)

private class DiaryContentCardPreviewProvider :
    PreviewParameterProvider<DiaryCardPreviewState> {
    override val values = sequenceOf(
        DiaryCardPreviewState(
            isShowCorrectedDiary = false,
            imageUrl = "",
            content = "이미지 & 텍스트",
            diffRanges = persistentListOf(),
        ),
        DiaryCardPreviewState(
            isShowCorrectedDiary = false,
            imageUrl = null,
            content = "I want to become a teacher future. Because I like child.",
            diffRanges = persistentListOf(),
        ),
        DiaryCardPreviewState(
            isShowCorrectedDiary = true,
            imageUrl = null,
            content =
                "Today I went to the cafe Conhas in Yeonnam to meet my teammates.\n " +
                    "I was planning to arrive around 1:30 p.m., but I got there at 2:20 " +
                    "because I overslept, as always.\n " +
                    "I wore rain boots and brought my favorite umbrella " +
                    "because the weather forecast said it would rain all day, " +
                    "but it wasn’t really raining much outside.\n " +
                    "I got kind of disappointed. But yes, no rain is better than rain, I guess.\n" +
                    "After arriving, I had a jambon arugula sandwich with a vanilla latte.\n " +
                    "Honestly, I should be more careful when I'm drinking milk " +
                    "because I get stomachaches easily, " +
                    "but I always order lattes.\nMy life feels like a disaster, a mess that I call myself.\n " +
                    "But they tasted really good, so I felt more motivated to work.\n " +
                    "I really liked this café because it's spacious, chill, " +
                    "and has a great atmosphere for focusing.\n " +
                    "I’ll definitely come back again soon!",
            diffRanges = persistentListOf(
                Pair(84, 164),
                Pair(278, 316),
                Pair(508, 583),
                Pair(740, 802),
            ),
        ),
    )
}

@Preview
@Composable
private fun DiaryCardPreview(
    @PreviewParameter(DiaryContentCardPreviewProvider::class) state: DiaryCardPreviewState,
) {
    HilingualTheme {
        DiaryCard(
            isShowCorrectedDiary = state.isShowCorrectedDiary,
            diaryContent = state.content,
            imageUrl = state.imageUrl,
            diffRanges = state.diffRanges,
            onImageClick = {},
        )
    }
}
