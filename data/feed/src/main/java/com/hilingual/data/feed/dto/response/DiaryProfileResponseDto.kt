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
data class DiaryProfileResponseDto(
    @SerialName("isMine")
    val isMine: Boolean,
    @SerialName("profile")
    val profile: ProfileInfoDto,
    @SerialName("diary")
    val diary: DiaryInfoDto
)

@Serializable
data class ProfileInfoDto(
    @SerialName("userId")
    val userId: Long,
    @SerialName("profileImg")
    val profileImg: String?,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("streak")
    val streak: Int
)

@Serializable
data class DiaryInfoDto(
    @SerialName("sharedDate")
    val sharedDate: Long,
    @SerialName("likeCount")
    val likeCount: Int,
    @SerialName("isLiked")
    val isLiked: Boolean
)
