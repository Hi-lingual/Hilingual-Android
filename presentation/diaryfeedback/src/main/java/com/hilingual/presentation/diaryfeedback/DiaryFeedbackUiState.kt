package com.hilingual.presentation.diaryfeedback

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class DiaryFeedbackUiState(
    val diaryId: Long = 0L,
    val isAI: Boolean = true,
    val diaryContent: DiaryContent = DiaryContent(),
    val feedbackList: ImmutableList<FeedbackContent>,
    val recommendExpressionList: ImmutableList<RecommendExpression>,
    val isLoading: Boolean = true
)

@Immutable
data class DiaryContent(
    val originalText: String = "",
    val aiText: String = "",
    val diffRanges: ImmutableList<Pair<Int, Int>> = persistentListOf(),
    val imageUrl: String? = null
)

@Immutable
data class FeedbackContent(
    val originalText: String,
    val feedbackText: String,
    val explain: String
)

@Immutable
data class RecommendExpression(
    val phraseId: Long,
    val phraseType: ImmutableList<String>,
    val phrase: String,
    val explanation: String,
    val reason: String,
    val isMarked: Boolean = false
)