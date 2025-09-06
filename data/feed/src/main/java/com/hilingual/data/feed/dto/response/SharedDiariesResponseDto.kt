package com.hilingual.data.feed.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SharedDiariesResponseDto(
    @SerialName("profile")
    val profile: SharedDiaryProfileDto,
    @SerialName("diaryList")
    val diaryList: List<SharedDiaryDto>
)

@Serializable
data class SharedDiaryProfileDto(
    @SerialName("profileImg")
    val profileImg: String?,
    @SerialName("nickname")
    val nickname: String
)

@Serializable
data class SharedDiaryDto(
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