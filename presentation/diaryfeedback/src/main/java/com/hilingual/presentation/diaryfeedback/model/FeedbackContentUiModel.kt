package com.hilingual.presentation.diaryfeedback.model

import androidx.compose.runtime.Immutable
import com.hilingual.data.diary.model.DiaryFeedbackModel

@Immutable
internal data class FeedbackContentUiModel(
    val originalText: String,
    val feedbackText: String,
    val explain: String
)

internal fun DiaryFeedbackModel.toState() = FeedbackContentUiModel(
    originalText = this.originalText,
    feedbackText = this.rewriteText,
    explain = this.explain,
)