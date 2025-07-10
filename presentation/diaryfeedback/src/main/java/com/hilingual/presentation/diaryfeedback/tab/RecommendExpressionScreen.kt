package com.hilingual.presentation.diaryfeedback.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilingual.core.designsystem.theme.HilingualTheme
import com.hilingual.presentation.diaryfeedback.DiaryFeedbackViewModel
import com.hilingual.presentation.diaryfeedback.RecommendExpression
import com.hilingual.presentation.diaryfeedback.component.RecommendExpressionCard
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun RecommendExpressionScreen(
    writtenDate: String,
    recommendExpressionList: ImmutableList<RecommendExpression>,
    isBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start,
        contentPadding = PaddingValues(vertical = 24.dp, horizontal = 16.dp),
        modifier = modifier
            .fillMaxSize()
            .background(HilingualTheme.colors.gray100)
    ) {
        item {
            Text(
                text = writtenDate,
                style = HilingualTheme.typography.bodySB14,
                color = HilingualTheme.colors.gray700
            )

            Spacer(Modifier.height(4.dp))
        }

        items(recommendExpressionList, key = { it.phraseId }) { item ->
            RecommendExpressionCard(
                phraseId = item.phraseId,
                phraseType = item.phraseType,
                phrase = item.phrase,
                explanation = item.explanation,
                reason = item.reason,
                isMarked = item.isMarked,
                onBookmarkClick = isBookmarkClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GrammarSpellingScreenPreview() {
    HilingualTheme {
        val dummyExpressions = DiaryFeedbackViewModel.dummyRecommendExpressions

        RecommendExpressionScreen(
            writtenDate = "7월 11일 금요일",
            recommendExpressionList = dummyExpressions,
            isBookmarkClick = {}
        )
    }
}