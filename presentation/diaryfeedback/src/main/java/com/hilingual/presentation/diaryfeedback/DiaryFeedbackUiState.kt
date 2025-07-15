package com.hilingual.presentation.diaryfeedback

import androidx.compose.runtime.Immutable
import com.hilingual.presentation.diaryfeedback.model.DiaryContentUiModel
import com.hilingual.presentation.diaryfeedback.model.FeedbackContentUiModel
import com.hilingual.presentation.diaryfeedback.model.RecommendExpressionUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class DiaryFeedbackUiState(
    val diaryId: Long = 9L,
    val writtenDate: String = "",
    val isAIWritten: Boolean = true,
    val diaryContent: DiaryContentUiModel = DiaryContentUiModel(),
    val feedbackList: ImmutableList<FeedbackContentUiModel> = persistentListOf(),
    val recommendExpressionList: ImmutableList<RecommendExpressionUiState> = persistentListOf(),
)