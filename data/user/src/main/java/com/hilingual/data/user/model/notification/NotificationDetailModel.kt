package com.hilingual.data.user.model.notification

import com.hilingual.data.user.dto.response.notification.NotificationDetailResponseDto

data class NotificationDetailModel(
    val title: String,
    val createdAt: String,
    val content: String
)

internal fun NotificationDetailResponseDto.toModel() = NotificationDetailModel(
    title = this.title,
    createdAt = this.createdAt,
    content = this.content
)
