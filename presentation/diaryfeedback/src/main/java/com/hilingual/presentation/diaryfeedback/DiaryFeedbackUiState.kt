package com.hilingual.presentation.diaryfeedback

import androidx.compose.runtime.Immutable
import com.hilingual.presentation.diaryfeedback.model.DiaryContentUiModel
import com.hilingual.presentation.diaryfeedback.model.FeedbackContentUiModel
import com.hilingual.presentation.diaryfeedback.model.RecommendExpressionUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class DiaryFeedbackUiState(
    val writtenDate: String = "",
    val diaryContent: DiaryContentUiModel = DiaryContentUiModel(),
    val feedbackList: ImmutableList<FeedbackContentUiModel> = persistentListOf(),
    val recommendExpressionList: ImmutableList<RecommendExpressionUiModel> = persistentListOf()
)
