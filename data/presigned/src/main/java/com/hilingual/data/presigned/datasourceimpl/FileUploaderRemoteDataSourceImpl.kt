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

import com.hilingual.data.presigned.datasource.FileUploaderRemoteDataSource
import com.hilingual.data.presigned.service.S3Service
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class FileUploaderRemoteDataSourceImpl @Inject constructor(
    private val s3Service: S3Service
) : FileUploaderRemoteDataSource {
    override suspend fun uploadFile(
        uploadUrl: String,
        contentType: String,
        file: RequestBody
    ): Response<Unit> = s3Service.uploadFile(
        uploadUrl = uploadUrl,
        contentType = contentType,
        file = file
    )
}
