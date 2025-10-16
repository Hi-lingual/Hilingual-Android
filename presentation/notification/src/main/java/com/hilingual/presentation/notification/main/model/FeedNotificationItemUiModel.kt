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
package com.hilingual.presentation.notification.main.model

import androidx.compose.runtime.Stable
import com.hilingual.data.user.model.notification.NotificationModel

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
    val targetId: Long
)

internal fun NotificationModel.toFeedStateOrNull(): FeedNotificationItemUiModel? {
    val currentType = type ?: return null
    val currentTargetId = targetId ?: return null

    return FeedNotificationItemUiModel(
        id = id,
        title = title,
        isRead = isRead,
        publishedAt = publishedAt,
        feedType = FeedNotificationType.valueOf(currentType),
        targetId = currentTargetId
    )
}
