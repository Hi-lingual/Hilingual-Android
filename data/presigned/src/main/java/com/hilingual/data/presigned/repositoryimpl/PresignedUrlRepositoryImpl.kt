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

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.presigned.datasource.PresignedUrlRemoteDataSource
import com.hilingual.data.presigned.model.PresignedUrlModel
import com.hilingual.data.presigned.model.toModel
import com.hilingual.data.presigned.repository.PresignedUrlRepository
import javax.inject.Inject

class PresignedUrlRepositoryImpl @Inject constructor(
    private val presignedUrlRemoteDataSource: PresignedUrlRemoteDataSource
) : PresignedUrlRepository {
    override suspend fun getPresignedUrl(
        purpose: String,
        contentType: String
    ): Result<PresignedUrlModel> = suspendRunCatching {
        presignedUrlRemoteDataSource.getPresignedUrl(purpose, contentType).data!!.toModel()
    }
}
