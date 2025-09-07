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
    val diary: LikedDiaryDto
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

@Serializable
data class LikedDiaryDto(
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
