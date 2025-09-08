package com.hilingual.data.user.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageRequestDto(
    @SerialName("fileKey")
    val fileKey: String,
    @SerialName("purpose")
    val purpose: String
)
