package com.hilingual.presentation.diaryfeedback

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class DiaryFeedbackUiState(
    val diaryId: Long = 9L,
    val writtenDate: String = "",
    val isAIWritten: Boolean = true, // 이거 빨리
    val diaryContent: DiaryContent = DiaryContent(),
    val feedbackList: ImmutableList<FeedbackContent> = persistentListOf(),
    val recommendExpressionList: ImmutableList<RecommendExpression> = persistentListOf(),
)

@Immutable
internal data class DiaryContent(
    val originalText: String = "",
    val aiText: String = "",
    val diffRanges: ImmutableList<Pair<Int, Int>> = persistentListOf(),
    val imageUrl: String? = null
)

@Immutable
internal data class FeedbackContent(
    val originalText: String,
    val feedbackText: String,
    val explain: String
)

@Immutable
internal data class RecommendExpression(
    val phraseId: Long,
    val phraseType: ImmutableList<String>,
    val phrase: String,
    val explanation: String,
    val reason: String,
    val isMarked: Boolean = false
)
