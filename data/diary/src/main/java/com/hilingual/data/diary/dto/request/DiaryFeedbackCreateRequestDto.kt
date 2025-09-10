package com.hilingual.data.diary.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiaryFeedbackCreateRequestDto(
    @SerialName("originalText")
    val originalText: String,
    @SerialName("date")
    val date: String,
    @SerialName("image")
    val image: ImageRequestDto? = null
)
