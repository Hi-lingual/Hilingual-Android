package com.hilingual.data.user.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterProfileRequestDto(
    @SerialName("image")
    val image: ImageRequestDto?,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("adAlarmAgree")
    val adAlarmAgree: Boolean
)
