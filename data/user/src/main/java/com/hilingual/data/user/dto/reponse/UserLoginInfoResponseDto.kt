package com.hilingual.data.user.dto.reponse

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
