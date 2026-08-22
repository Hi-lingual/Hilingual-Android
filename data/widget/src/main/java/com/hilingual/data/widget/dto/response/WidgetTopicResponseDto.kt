package com.hilingual.data.widget.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WidgetTopicResponseDto(
    @SerialName("date")
    val date: String,
    @SerialName("topicEn")
    val topicEn: String?,
    @SerialName("isWrittenToday")
    val isWrittenToday: Boolean?,
)
