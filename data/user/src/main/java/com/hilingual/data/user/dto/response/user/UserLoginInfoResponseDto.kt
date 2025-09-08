package com.hilingual.data.user.dto.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserLoginInfoResponseDto(
    @SerialName("profileImg")
    val profileImg: String,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("provider")
    val provider: String
)
