package com.hilingual.data.user.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileImageRequestDto(
    @SerialName("image")
    val image: ImageRequestDto? = null
)
