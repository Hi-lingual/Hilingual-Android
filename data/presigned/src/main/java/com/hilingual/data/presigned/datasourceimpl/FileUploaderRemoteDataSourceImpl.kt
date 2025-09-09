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
