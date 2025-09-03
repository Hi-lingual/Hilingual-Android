package com.hilingual.presentation.notification.main

import androidx.compose.runtime.Immutable
import com.hilingual.presentation.notification.main.model.FeedNotificationItemModel
import com.hilingual.presentation.notification.main.model.FeedNotificationType
import com.hilingual.presentation.notification.main.model.NoticeNotificationItemModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class NotificationUiState(
    val feedNotifications: ImmutableList<FeedNotificationItemModel> = persistentListOf(),
    val noticeNotifications: ImmutableList<NoticeNotificationItemModel> = persistentListOf()
) {
    companion object {
        val Fake = NotificationUiState(
            feedNotifications = persistentListOf(
                FeedNotificationItemModel(
                    noticeId = 0,
                    type = FeedNotificationType.LIKE_DIARY,
                    title = "방금 이병건님이 당신의 8월 14일 일기에 공감했습니다.",
                    targetId = 100,
                    date = "2025.08.15",
                    isRead = false
                ),
                FeedNotificationItemModel(
                    noticeId = 1,
                    type = FeedNotificationType.FOLLOW_USER,
                    title = "토착왜구맨님이 당신을 팔로우하기 시작했습니다.",
                    targetId = 200,
                    date = "2025.08.15",
                    isRead = true
                )
            ),
            noticeNotifications = persistentListOf(
                NoticeNotificationItemModel(
                    id = 0,
                    title = "v.1.1.0 업데이트 알림",
                    date = "2025.08.15",
                    isRead = false
                ),
                NoticeNotificationItemModel(
                    id = 1,
                    title = "시스템 점검에 따른 사용 중단 안내",
                    date = "2025.08.15",
                    isRead = true
                )
            )
        )
    }
}
