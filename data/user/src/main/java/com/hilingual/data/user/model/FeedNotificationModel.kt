package com.hilingual.data.user.model

import com.hilingual.data.user.dto.reponse.FeedNotificationResponseDto

data class FeedNotificationModel(
    override val noticeId: Long,
    val type: FeedType,
    override val title: String,
    val targetId: String,
    override val isRead: Boolean,
    override val publishedAt: String
) : NotificationModel

fun FeedNotificationResponseDto.toFeedNotificationModel(): FeedNotificationModel {
    return FeedNotificationModel(
        noticeId = this.noticeId,
        type = FeedType.valueOf(this.type),
        title = this.title,
        targetId = this.targetId.toString(),
        isRead = this.isRead,
        publishedAt = this.publishedAt
    )
}
