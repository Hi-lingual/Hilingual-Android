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

import com.hilingual.data.feed.dto.response.SharedDiariesResponseDto
import com.hilingual.data.feed.dto.response.SharedDiaryProfileDto

data class SharedDiariesModel(
    val profile: SharedDiaryProfileModel,
    val diaryList: List<FeedItemDiaryModel>
)

data class SharedDiaryProfileModel(
    val profileImg: String?,
    val nickname: String
)

internal fun SharedDiariesResponseDto.toModel(): SharedDiariesModel = SharedDiariesModel(
    profile = this.profile.toModel(),
    diaryList = this.diaryList.map { diaryDto ->
        FeedItemDiaryModel(
            diaryId = diaryDto.diaryId,
            sharedDate = diaryDto.sharedDate,
            likeCount = diaryDto.likeCount,
            isLiked = diaryDto.isLiked,
            diaryImg = diaryDto.diaryImg,
            originalText = diaryDto.originalText
        )
    }
)

internal fun SharedDiaryProfileDto.toModel(): SharedDiaryProfileModel = SharedDiaryProfileModel(
    profileImg = this.profileImg,
    nickname = this.nickname
)
