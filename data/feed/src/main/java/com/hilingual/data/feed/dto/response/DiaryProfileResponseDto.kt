package com.hilingual.data.feed.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiaryProfileResponseDto(
    @SerialName("isMine")
    val isMine: Boolean,
    @SerialName("profile")
    val profile: ProfileInfoDto,
    @SerialName("diary")
    val diary: DiaryInfoDto
)

@Serializable
data class ProfileInfoDto(
    @SerialName("userId")
    val userId: Long,
    @SerialName("profileImg")
    val profileImg: String?,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("streak")
    val streak: Int
)

@Serializable
data class DiaryInfoDto(
    @SerialName("sharedDate")
    val sharedDate: Long,
    @SerialName("likeCount")
    val likeCount: Int,
    @SerialName("isLiked")
    val isLiked: Boolean
)
