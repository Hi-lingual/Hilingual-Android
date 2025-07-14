package com.hilingual.data.diary.model

import com.hilingual.data.diary.dto.response.RecommendExpressionResult

data class DiaryRecommendExpressionModel(
    val phraseId: Long,
    val phraseType: List<String>,
    val phrase: String,
    val explanation: String,
    val reason: String,
    val isMarked: Boolean = false
)

internal fun RecommendExpressionResult.toModel() = DiaryRecommendExpressionModel(
    phraseId = this.phraseId,
    phraseType = this.phraseType,
    phrase = this.phrase,
    explanation = this.explanation,
    reason = this.reason,
    isMarked = this.isBookmarked
)