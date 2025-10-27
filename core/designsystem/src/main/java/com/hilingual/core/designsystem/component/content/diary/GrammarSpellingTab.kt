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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.model.DiaryContent
import com.hilingual.core.designsystem.model.FeedbackContent
import com.hilingual.core.designsystem.theme.HilingualTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun GrammarSpellingTab(
    listState: LazyListState,
    writtenDate: String,
    diaryContent: DiaryContent,
    feedbackList: ImmutableList<FeedbackContent>,
    isAIWrittenDiary: Boolean,
    onImageClick: () -> Unit,
    onToggleViewMode: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 40.dp),
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.gray100)
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = writtenDate,
                    style = HilingualTheme.typography.bodySB16,
                    color = HilingualTheme.colors.gray700
                )
                DiaryViewModeToggle(
                    isAIWritten = isAIWrittenDiary,
                    onToggle = onToggleViewMode
                )
            }
            Spacer(Modifier.height(12.dp))
            with(diaryContent) {
                DiaryCard(
                    isAIWritten = isAIWrittenDiary,
                    diaryContent = if (isAIWrittenDiary) aiText else originalText,
                    diffRanges = diffRanges,
                    imageUrl = imageUrl,
                    onImageClick = onImageClick
                )
            }
            Spacer(Modifier.height(24.dp))
        }

        item {
            FeedbackTitle(feedbackList.size)
            Spacer(Modifier.height(12.dp))
        }

        if (feedbackList.isEmpty()) {
            item {
                FeedbackEmptyCard()
            }
        } else {
            itemsIndexed(
                feedbackList,
                key = { _, feedback -> Pair(feedback.originalText, feedback.feedbackText) }
            ) { index, content ->
                with(content) {
                    FeedbackCard(
                        originalText = originalText,
                        feedbackText = feedbackText,
                        explain = explain,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (index != feedbackList.lastIndex) Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun FeedbackTitle(feedbackSize: Int) {
    Text(
        text = if (feedbackSize == 0) AnnotatedString("일기에서 발견된 피드백 알려드릴게요!") else getFeedbackTitleAnnotatedString(feedbackSize),
        style = HilingualTheme.typography.bodySB16,
        color = HilingualTheme.colors.black
    )
}

@Composable
private fun getFeedbackTitleAnnotatedString(
    feedbackSize: Int
) = buildAnnotatedString {
    append("주요 피드백 ")
    withStyle(style = SpanStyle(color = HilingualTheme.colors.hilingualOrange)) {
        append(feedbackSize.toString())
    }
    append("개 알려드릴게요!")
}

@Preview(showBackground = true)
@Composable
private fun GrammarSpellingTabPreview() {
    var isAIWritten by remember { mutableStateOf(true) }
    HilingualTheme {
        GrammarSpellingTab(
            listState = rememberLazyListState(),
            writtenDate = "7월 11일 금요일",
            diaryContent = DiaryContent(),
            feedbackList = persistentListOf(),
            isAIWrittenDiary = isAIWritten,
            onImageClick = {},
            onToggleViewMode = { isAIWritten = it }
        )
    }
}
