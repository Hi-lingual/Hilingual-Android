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
package com.hilingual.data.presigned.repositoryimpl

import android.content.Context
import android.net.Uri
import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.core.network.ContentUriRequestBody
import com.hilingual.data.presigned.datasource.FileUploaderRemoteDataSource
import com.hilingual.data.presigned.repository.FileUploaderRepository
import com.hilingual.data.presigned.repository.PresignedUrlRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FileUploaderRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val presignedUrlRepository: PresignedUrlRepository,
    private val fileUploaderRemoteDataSource: FileUploaderRemoteDataSource
) : FileUploaderRepository {

    override suspend fun uploadFile(uri: Uri, purpose: String): Result<String> = suspendRunCatching {
        val contentType = "image/webp"

        val presignedUrlModel = presignedUrlRepository.getPresignedUrl(
            purpose = purpose,
            contentType = contentType
        ).getOrThrow()

        val imageConfig = ContentUriRequestBody.ImageConfig.WEBP

        val requestBody = ContentUriRequestBody(context, uri, imageConfig)
        requestBody.prepareImage().getOrThrow()

        val response = fileUploaderRemoteDataSource.uploadFile(
            uploadUrl = presignedUrlModel.uploadUrl,
            contentType = contentType,
            file = requestBody
        )

        if (!response.isSuccessful) {
            throw Exception("S3 upload failed: ${response.errorBody()?.string()}")
        }

        presignedUrlModel.fileKey
    }
}
