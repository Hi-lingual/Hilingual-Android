package com.hilingual.data.feed.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedItemDto(
    @SerialName("profile")
    val profile: FeedItemProfileDto,
    @SerialName("diary")
    val diary: FeedItemDiaryDto
)

@Serializable
data class FeedItemProfileDto(
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
data class FeedItemDiaryDto(
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
