package com.hilingual.data.user.dto.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateNicknameResponseDto(
    @SerialName("nickname")
    val nickname: String,
)
