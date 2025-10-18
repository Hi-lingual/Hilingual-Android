/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.presentation.notification.main

import androidx.compose.runtime.Immutable
import com.hilingual.presentation.notification.main.model.FeedNotificationItemUiModel
import com.hilingual.presentation.notification.main.model.FeedNotificationType
import com.hilingual.presentation.notification.main.model.NoticeCategoryType
import com.hilingual.presentation.notification.main.model.NoticeNotificationItemUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

enum class NotificationTab {
    FEED,
    NOTIFICATION
}

@Immutable
data class NotificationUiState(
    val isFeedRefreshing: Boolean = false,
    val isNoticeRefreshing: Boolean = false,
    val feedNotifications: ImmutableList<FeedNotificationItemUiModel> = persistentListOf(),
    val noticeNotifications: ImmutableList<NoticeNotificationItemUiModel> = persistentListOf()
) {
    companion object {
        val Fake = NotificationUiState(
            feedNotifications = persistentListOf(
                FeedNotificationItemUiModel(
                    id = 0,
                    feedType = FeedNotificationType.LIKE_DIARY,
                    title = "방금 이병건님이 당신의 8월 14일 일기에 공감했습니다.",
                    targetId = 100L,
                    publishedAt = "2025.08.15",
                    isRead = false
                ),
                FeedNotificationItemUiModel(
                    id = 1,
                    feedType = FeedNotificationType.FOLLOW_USER,
                    title = "토착왜구맨님이 당신을 팔로우하기 시작했습니다.",
                    targetId = 200L,
                    publishedAt = "2025.08.15",
                    isRead = true
                )
            ),
            noticeNotifications = persistentListOf(
                NoticeNotificationItemUiModel(
                    id = 0,
                    noticeCategory = NoticeCategoryType.SYSTEM,
                    title = "v.1.1.0 업데이트 알림",
                    publishedAt = "2025.08.15",
                    isRead = false
                ),
                NoticeNotificationItemUiModel(
                    id = 1,
                    noticeCategory = NoticeCategoryType.MARKETING,
                    title = "시스템 점검에 따른 사용 중단 안내",
                    publishedAt = "2025.08.15",
                    isRead = true
                )
            )
        )
    }
}
