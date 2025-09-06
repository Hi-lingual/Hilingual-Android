package com.hilingual.data.feed.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDto(
    @SerialName("userList")
    val userList: List<UserItemDto>
)

@Serializable
data class UserItemDto(
    @SerialName("userId")
    val userId: Long,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("profileImg")
    val profileImg: String,
    @SerialName("isFollowing")
    val isFollowing: Boolean,
    @SerialName("isFollowed")
    val isFollowed: Boolean
)
