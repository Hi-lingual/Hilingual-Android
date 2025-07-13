package com.hilingual.presentation.diaryfeedback.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.diaryfeedback.DiaryContent
import com.hilingual.presentation.diaryfeedback.DiaryFeedbackViewModel
import com.hilingual.presentation.diaryfeedback.FeedbackContent
import com.hilingual.presentation.diaryfeedback.component.DiaryViewModeToggle
import com.hilingual.presentation.diaryfeedback.component.DiaryCard
import com.hilingual.presentation.diaryfeedback.component.FeedbackCard
import com.hilingual.presentation.diaryfeedback.component.FeedbackEmptyCard
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun GrammarSpellingScreen(
    writtenDate: String,
    diaryContent: DiaryContent,
    feedbackList: ImmutableList<FeedbackContent>,
    onToggleDiaryViewMode: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isAIWritten: Boolean = true
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 24.dp, horizontal = 16.dp),
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.gray100)
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = writtenDate,
                    style = HilingualTheme.typography.bodySB14,
                    color = HilingualTheme.colors.gray700
                )
                DiaryViewModeToggle(
                    isAIWritten = isAIWritten,
                    onToggle = { onToggleDiaryViewMode(!isAIWritten) }
                )
            }
            Spacer(Modifier.height(12.dp))
            with(diaryContent) {
                DiaryCard(
                    isAIWritten = isAIWritten,
                    diaryContent = if (isAIWritten) aiText else originalText,
                    diffRanges = diffRanges,
                    imageUrl = imageUrl
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
    feedbackSize: Int,
) = buildAnnotatedString {
    append("주요 피드백 ")
    withStyle(style = SpanStyle(color = HilingualTheme.colors.hilingualOrange)) {
        append(feedbackSize.toString())
    }
    append("개 알려드릴게요!")
}

@Preview(showBackground = true)
@Composable
private fun GrammarSpellingScreenPreview() {
    HilingualTheme {
        var isAI by remember { mutableStateOf(true) }

        val dummyDiary = DiaryFeedbackViewModel.dummyDiaryContent

        GrammarSpellingScreen(
            writtenDate = "7월 11일 금요일",
            isAIWritten = isAI,
            onToggleDiaryViewMode = { isAI = !isAI },
            diaryContent = dummyDiary,
            feedbackList = DiaryFeedbackViewModel.dummyFeedbacks
        )
    }
}
