package com.hilingual.data.user.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PatchFcmTokenRequestDto(
    @SerialName("uuid")
    val uuid: String,
    @SerialName("fcmToken")
    val fcmToken: String
)
