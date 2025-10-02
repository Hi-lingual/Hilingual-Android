package com.hilingual.data.presigned.datasourceimpl

import com.hilingual.core.network.model.BaseResponse
import com.hilingual.data.presigned.datasource.PresignedUrlRemoteDataSource
import com.hilingual.data.presigned.dto.request.PresignedUrlRequestDto
import com.hilingual.data.presigned.dto.response.PresignedUrlResponseDto
import com.hilingual.data.presigned.service.PresignedUrlService
import javax.inject.Inject

class PresignedUrlRemoteDataSourceImpl @Inject constructor(
    private val presignedUrlService: PresignedUrlService
) : PresignedUrlRemoteDataSource {
    override suspend fun getPresignedUrl(
        purpose: String,
        contentType: String
    ): BaseResponse<PresignedUrlResponseDto> = presignedUrlService.getPresignedUrl(
        PresignedUrlRequestDto(
            purpose = purpose,
            contentType = contentType
        )
    )
}
