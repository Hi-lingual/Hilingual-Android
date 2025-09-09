package com.hilingual.data.presigned.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class PresignedUrlRequestDto(
    val purpose: String,
    val contentType: String
)
