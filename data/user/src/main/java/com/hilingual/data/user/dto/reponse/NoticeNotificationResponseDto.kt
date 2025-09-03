package com.hilingual.data.user.dto.reponse

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoticeNotificationResponseDto(
    @SerialName("noticeId")
    val noticeId: Long,
    @SerialName("category")
    val category: String,
    @SerialName("title")
    val title: String,
    @SerialName("isRead")
    val isRead: Boolean,
    @SerialName("publishedAt")
    val publishedAt: String
)
