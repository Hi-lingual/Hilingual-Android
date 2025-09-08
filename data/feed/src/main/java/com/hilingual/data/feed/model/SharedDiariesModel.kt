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
    diaryList = this.diaryList.map { it.toFeedItemDiaryModel() }
)

internal fun SharedDiaryProfileDto.toModel(): SharedDiaryProfileModel = SharedDiaryProfileModel(
    profileImg = this.profileImg,
    nickname = this.nickname
)
