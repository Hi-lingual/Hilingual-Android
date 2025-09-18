package com.hilingual.data.user.model.notification

import com.hilingual.data.user.dto.response.notification.NotificationResponseDto

data class NotificationModel(
    val id: Long,
    val type: String?,
    val category: String?,
    val title: String,
    val targetId: Long?,
    val isRead: Boolean,
    val publishedAt: String
)

internal fun NotificationResponseDto.toModel() = NotificationModel(
    id = this.noticeId,
    type = this.type,
    category = this.category,
    title = this.title,
    targetId = this.targetId,
    isRead = this.isRead,
    publishedAt = this.publishedAt
)
