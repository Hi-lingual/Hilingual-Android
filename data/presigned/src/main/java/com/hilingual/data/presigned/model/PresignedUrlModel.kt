package com.hilingual.data.presigned.model

import com.hilingual.data.presigned.dto.response.PresignedUrlResponseDto

data class PresignedUrlModel(
    val fileKey: String,
    val uploadUrl: String
)

internal fun PresignedUrlResponseDto.toModel() = PresignedUrlModel(
    fileKey = this.fileKey,
    uploadUrl = this.uploadUrl
)
