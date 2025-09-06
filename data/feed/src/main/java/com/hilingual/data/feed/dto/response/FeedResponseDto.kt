package com.hilingual.data.feed.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedResponseDto(
    @SerialName("diaryList")
    val diaryList: List<FeedResultDto>
)

@Serializable
data class FeedResultDto(
    @SerialName("profile")
    val profile: FeedProfileDto,
    @SerialName("diary")
    val diary: FeedDiaryPreviewDto
)

@Serializable
data class FeedProfileDto(
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
data class FeedDiaryPreviewDto(
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
