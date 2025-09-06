package com.hilingual.data.user.dto.reponse

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FollowingResponseDto(
    @SerialName("followingList")
    val userList: List<UserItemDto>
)
