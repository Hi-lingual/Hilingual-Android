package com.hilingual.data.user.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileRequestDto(
    @SerialName("profileImg")
    val profileImg: String,
    @SerialName("nickname")
    val nickname: String
)
