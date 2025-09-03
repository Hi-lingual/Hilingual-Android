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

import androidx.compose.runtime.Immutable
import com.hilingual.data.user.model.FeedNotificationModel

enum class FeedNotificationType {
    LIKE_DIARY,
    FOLLOW_USER
}

@Immutable
data class FeedNotificationItemModel(
    val noticeId: Long,
    val type: FeedNotificationType,
    val title: String,
    val targetId: Long,
    val date: String,
    val isRead: Boolean
)

internal fun FeedNotificationModel.toUiModel(): FeedNotificationItemModel {
    return FeedNotificationItemModel(
        noticeId = this.noticeId,
        type = FeedNotificationType.valueOf(this.type.name),
        title = this.title,
        targetId = this.targetId.toLongOrNull() ?: 0L,
        date = this.publishedAt,
        isRead = this.isRead
    )
}
