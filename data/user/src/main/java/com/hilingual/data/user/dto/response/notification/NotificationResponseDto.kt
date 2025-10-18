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
package com.hilingual.data.user.dto.response.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationResponseDto(
    @SerialName("noticeId")
    val noticeId: Long,
    @SerialName("type")
    val type: String? = null,
    @SerialName("category")
    val category: String? = null,
    @SerialName("title")
    val title: String,
    @SerialName("targetId")
    val targetId: Long? = null,
    @SerialName("isRead")
    val isRead: Boolean,
    @SerialName("publishedAt")
    val publishedAt: String
)
