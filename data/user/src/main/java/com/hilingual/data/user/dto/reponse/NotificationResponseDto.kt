package com.hilingual.data.user.dto.reponse

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationResponseDto(
    @SerialName("noticeId")
    val noticeId: Long,
    @SerialName("type")
    val type: String? = null,
    @SerialName("category")
    val category: String? = null,
    @SerialName("title")
    val title: String,
    @SerialName("targetId")
    val targetId: String? = null,
    @SerialName("isRead")
    val isRead: Boolean,
    @SerialName("publishedAt") val publishedAt: String
)
