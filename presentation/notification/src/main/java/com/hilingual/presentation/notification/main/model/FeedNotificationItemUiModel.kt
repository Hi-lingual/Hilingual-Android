package com.hilingual.presentation.notification.main.model

import androidx.compose.runtime.Stable
import com.hilingual.data.user.model.NotificationModel

enum class FeedNotificationType {
    LIKE_DIARY,
    FOLLOW_USER
}

@Stable
data class FeedNotificationItemUiModel(
    val id: Long,
    val title: String,
    val isRead: Boolean,
    val publishedAt: String,
    val feedType: FeedNotificationType,
    val targetId: String
)

internal fun NotificationModel.toFeedUiModel(): FeedNotificationItemUiModel = FeedNotificationItemUiModel(
    id = id,
    title = title,
    isRead = isRead,
    publishedAt = publishedAt,
    feedType = FeedNotificationType.valueOf(type!!),
    targetId = targetId!!
)
