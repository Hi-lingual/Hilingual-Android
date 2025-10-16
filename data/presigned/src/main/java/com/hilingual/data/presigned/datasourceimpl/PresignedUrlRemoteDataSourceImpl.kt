/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
