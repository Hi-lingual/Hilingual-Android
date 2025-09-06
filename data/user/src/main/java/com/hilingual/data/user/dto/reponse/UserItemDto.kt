package com.hilingual.data.user.dto.reponse

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
