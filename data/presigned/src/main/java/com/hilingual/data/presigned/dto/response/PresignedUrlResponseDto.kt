package com.hilingual.data.presigned.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class PresignedUrlResponseDto(
    val fileKey: String,
    val uploadUrl: String
)
