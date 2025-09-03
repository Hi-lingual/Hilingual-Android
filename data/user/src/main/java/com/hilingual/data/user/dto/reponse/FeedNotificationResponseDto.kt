package com.hilingual.data.user.dto.reponse

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedNotificationResponseDto(
    @SerialName("noticeId")
    val noticeId: Long,
    @SerialName("type")
    val type: String,
    @SerialName("title")
    val title: String,
    @SerialName("targetId")
    val targetId: Long,
    @SerialName("isRead")
    val isRead: Boolean,
    @SerialName("publishedAt")
    val publishedAt: String
)
