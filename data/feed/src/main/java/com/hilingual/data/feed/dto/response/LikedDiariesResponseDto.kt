package com.hilingual.data.feed.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LikedDiariesResponseDto(
    @SerialName("diaryList")
    val diaryList: List<LikedDiaryItemDto>
)

@Serializable
data class LikedDiaryItemDto(
    @SerialName("profile")
    val profile: LikedDiaryProfileDto,
    @SerialName("diary")
    val diary: DiaryDto
)

@Serializable
data class LikedDiaryProfileDto(
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
