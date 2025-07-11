package com.hilingual.data.user.dto.reponse

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NicknameResponseDto(
    @SerialName("isAvailable")
    val isAvailable: Boolean
)