package com.hilingual.data.calendar.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicResponseDto(
    @SerialName("topicKor")
    val topicKor: String,
    @SerialName("topicEn")
    val topicEn: String,
    @SerialName("remainingTime")
    val remainingTime: Int
)
