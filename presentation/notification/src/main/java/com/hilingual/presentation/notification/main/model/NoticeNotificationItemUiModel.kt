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

internal fun NotificationModel.toNoticeStateOrNull(): NoticeNotificationItemUiModel? {
    val currentCategory = category ?: return null

    return NoticeNotificationItemUiModel(
        id = id,
        title = title,
        isRead = isRead,
        publishedAt = publishedAt,
        noticeCategory = NoticeCategoryType.valueOf(currentCategory)
    )
}
