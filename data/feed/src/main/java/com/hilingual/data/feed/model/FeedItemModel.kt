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

import com.hilingual.data.feed.dto.response.FeedItemDto

data class FeedItemModel(
    val profile: FeedItemProfileModel,
    val diary: FeedItemDiaryModel
)

data class FeedItemProfileModel(
    val userId: Long,
    val isMine: Boolean,
    val profileImg: String?,
    val nickname: String,
    val streak: Int
)

data class FeedItemDiaryModel(
    val diaryId: Long,
    val sharedDate: Long,
    val likeCount: Int,
    val isLiked: Boolean,
    val diaryImg: String?,
    val originalText: String
)

internal fun FeedItemDto.toModel(): FeedItemModel = FeedItemModel(
    profile = with(this.profile) {
        FeedItemProfileModel(
            userId = this.userId,
            isMine = this.isMine,
            profileImg = this.profileImg,
            nickname = this.nickname,
            streak = this.streak
        )
    },
    diary = with(this.diary) {
        FeedItemDiaryModel(
            diaryId = this.diaryId,
            sharedDate = this.sharedDate,
            likeCount = this.likeCount,
            isLiked = this.isLiked,
            diaryImg = this.diaryImg,
            originalText = this.originalText
        )
    }
)
