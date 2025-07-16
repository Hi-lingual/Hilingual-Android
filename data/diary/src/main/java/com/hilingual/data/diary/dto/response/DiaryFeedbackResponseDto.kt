package com.hilingual.data.diary.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiaryFeedbackResponseDto(
    @SerialName("feedbackList")
    val feedbackList: List<FeedbackContent>
)

@Serializable
data class FeedbackContent(
    @SerialName("original")
    val original: String,
    @SerialName("rewrite")
    val rewrite: String,
    @SerialName("explain")
    val explain: String
)
