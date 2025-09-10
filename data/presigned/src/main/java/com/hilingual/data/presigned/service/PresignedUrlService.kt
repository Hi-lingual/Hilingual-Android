package com.hilingual.data.presigned.service

import com.hilingual.core.network.model.BaseResponse
import com.hilingual.data.presigned.dto.request.PresignedUrlRequestDto
import com.hilingual.data.presigned.dto.response.PresignedUrlResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface PresignedUrlService {
    @POST("/api/v1/presigned-urls")
    suspend fun getPresignedUrl(
        @Body request: PresignedUrlRequestDto
    ): BaseResponse<PresignedUrlResponseDto>
}
