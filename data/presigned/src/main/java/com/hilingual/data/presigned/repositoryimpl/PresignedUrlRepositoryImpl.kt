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
