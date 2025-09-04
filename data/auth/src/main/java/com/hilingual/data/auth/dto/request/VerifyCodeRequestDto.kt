package com.hilingual.data.auth.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyCodeRequestDto(
    @SerialName("code")
    val code: String
)
