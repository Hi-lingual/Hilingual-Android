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
package com.hilingual.data.feed.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedItemDto(
    @SerialName("profile")
    val profile: FeedItemProfileDto,
    @SerialName("diary")
    val diary: FeedItemDiaryDto
)

@Serializable
data class FeedItemProfileDto(
    @SerialName("userId")
    val userId: Long,
    @SerialName("isMine")
    val isMine: Boolean,
    @SerialName("profileImg")
    val profileImg: String?,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("streak")
    val streak: Int
)

@Serializable
data class FeedItemDiaryDto(
    @SerialName("diaryId")
    val diaryId: Long,
    @SerialName("sharedDate")
    val sharedDate: Long,
    @SerialName("likeCount")
    val likeCount: Int,
    @SerialName("isLiked")
    val isLiked: Boolean,
    @SerialName("diaryImg")
    val diaryImg: String?,
    @SerialName("originalText")
    val originalText: String
)
