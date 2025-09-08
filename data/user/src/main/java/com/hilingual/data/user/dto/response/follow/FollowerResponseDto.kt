package com.hilingual.data.user.dto.response.follow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FollowerResponseDto(
    @SerialName("followerList")
    val userList: List<UserItemDto>
)
