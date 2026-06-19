package com.hilingual.data.user.dto.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterProfileResponseDto(
    @SerialName("userId")
    val userId: Long,
)
