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
package com.hilingual.data.feed.model

import com.hilingual.data.feed.dto.response.DiaryProfileResponseDto

data class FeedDiaryProfileModel(
    val isMine: Boolean,
    val profile: ProfileModel,
    val diary: DiaryInfoModel
)

data class ProfileModel(
    val userId: Long,
    val profileImg: String?,
    val nickname: String,
    val streak: Int
)

data class DiaryInfoModel(
    val sharedDate: Long,
    val likeCount: Int,
    val isLiked: Boolean
)

internal fun DiaryProfileResponseDto.toModel() = FeedDiaryProfileModel(
    isMine = this.isMine,
    profile = with(this.profile) {
        ProfileModel(
            userId = userId,
            profileImg = profileImg,
            nickname = nickname,
            streak = streak
        )
    },
    diary = with(this.diary) {
        DiaryInfoModel(
            sharedDate = sharedDate,
            likeCount = likeCount,
            isLiked = isLiked
        )
    }
)
