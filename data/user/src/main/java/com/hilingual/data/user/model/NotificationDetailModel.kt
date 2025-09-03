package com.hilingual.data.user.model

import com.hilingual.data.user.dto.reponse.NotificationDetailResponseDto

data class NotificationDetailModel(
    val title: String,
    val createdAt: String,
    val content: String
)

fun NotificationDetailResponseDto.toModel(): NotificationDetailModel = NotificationDetailModel(
    title = this.title,
    createdAt = this.createdAt,
    content = this.content
)
