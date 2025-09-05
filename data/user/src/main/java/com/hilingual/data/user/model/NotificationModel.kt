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
package com.hilingual.data.user.model

import com.hilingual.data.user.dto.reponse.NotificationResponseDto

data class NotificationModel(
    val id: Long,
    val type: String?,
    val category: String?,
    val title: String,
    val targetId: String?,
    val isRead: Boolean,
    val publishedAt: String
)

internal fun NotificationResponseDto.toModel(): NotificationModel = NotificationModel(
    id = this.noticeId,
    type = this.type,
    category = this.category,
    title = this.title,
    targetId = this.targetId,
    isRead = this.isRead,
    publishedAt = this.publishedAt
)
