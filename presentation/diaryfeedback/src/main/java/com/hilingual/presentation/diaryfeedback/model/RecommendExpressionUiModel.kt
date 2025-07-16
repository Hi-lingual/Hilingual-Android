package com.hilingual.presentation.diaryfeedback.model

import androidx.compose.runtime.Immutable
import com.hilingual.data.diary.model.DiaryRecommendExpressionModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
internal data class RecommendExpressionUiModel(
    val phraseId: Long,
    val phraseType: ImmutableList<String>,
    val phrase: String,
    val explanation: String,
    val reason: String,
    val isMarked: Boolean = false
)

internal fun DiaryRecommendExpressionModel.toState() = RecommendExpressionUiModel(
    phraseId = this.phraseId,
    phraseType = this.phraseType.toImmutableList(),
    phrase = this.phrase,
    explanation = this.explanation,
    reason = this.reason,
    isMarked = this.isMarked
)
