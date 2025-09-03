package com.hilingual.data.user.model

import com.hilingual.data.user.dto.reponse.NoticeNotificationResponseDto

data class NoticeNotificationModel(
    override val noticeId: Long,
    val category: NoticeCategory,
    override val title: String,
    override val isRead: Boolean,
    override val publishedAt: String
) : NotificationModel

fun NoticeNotificationResponseDto.toNoticeNotificationModel(): NoticeNotificationModel {
    return NoticeNotificationModel(
        noticeId = this.noticeId,
        category = NoticeCategory.valueOf(this.category),
        title = this.title,
        isRead = this.isRead,
        publishedAt = this.publishedAt
    )
}
