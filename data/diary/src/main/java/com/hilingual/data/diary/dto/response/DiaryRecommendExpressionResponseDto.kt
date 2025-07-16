package com.hilingual.data.diary.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiaryRecommendExpressionResponseDto(
    @SerialName("phraseList")
    val phraseList: List<RecommendExpressionResult>
)

@Serializable
data class RecommendExpressionResult(
    @SerialName("phraseId")
    val phraseId: Long,
    @SerialName("phraseType")
    val phraseType: List<String>,
    @SerialName("phrase")
    val phrase: String,
    @SerialName("explanation")
    val explanation: String,
    @SerialName("reason")
    val reason: String,
    @SerialName("isBookmarked")
    val isBookmarked: Boolean
)
