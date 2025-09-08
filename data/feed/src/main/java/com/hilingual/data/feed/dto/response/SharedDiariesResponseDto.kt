package com.hilingual.data.feed.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SharedDiariesResponseDto(
    @SerialName("profile")
    val profile: SharedDiaryProfileDto,
    @SerialName("diaryList")
    val diaryList: List<FeedItemDiaryDto>
)

@Serializable
data class SharedDiaryProfileDto(
    @SerialName("profileImg")
    val profileImg: String?,
    @SerialName("nickname")
    val nickname: String
)
