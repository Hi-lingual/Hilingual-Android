package com.hilingual.data.user.dto.response.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationSettingsResponseDto(
    @SerialName("marketing")
    val marketing: Boolean,
    @SerialName("feed")
    val feed: Boolean
)
