package com.hilingual.data.presigned.datasource

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.presigned.dto.response.PresignedUrlResponseDto

interface PresignedUrlRemoteDataSource {
    suspend fun getPresignedUrl(purpose: String, contentType: String): BaseResponse<PresignedUrlResponseDto>
}
