package com.hilingual.data.user.dto.response.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDetailResponseDto(
    @SerialName("title")
    val title: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("content")
    val content: String
)
