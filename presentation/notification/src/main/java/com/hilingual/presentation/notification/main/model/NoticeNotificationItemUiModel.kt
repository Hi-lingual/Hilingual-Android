package com.hilingual.presentation.notification.main.model

import androidx.compose.runtime.Stable
import com.hilingual.data.user.model.NotificationModel

enum class NoticeCategoryType {
    SYSTEM,
    MARKETING
}

@Stable
data class NoticeNotificationItemUiModel(
    val id: Long,
    val title: String,
    val isRead: Boolean,
    val publishedAt: String,
    val noticeCategory: NoticeCategoryType
)

internal fun NotificationModel.toNoticeUiModel(): NoticeNotificationItemUiModel = NoticeNotificationItemUiModel(
    id = id,
    title = title,
    isRead = isRead,
    publishedAt = publishedAt,
    noticeCategory = NoticeCategoryType.valueOf(category!!)
)
