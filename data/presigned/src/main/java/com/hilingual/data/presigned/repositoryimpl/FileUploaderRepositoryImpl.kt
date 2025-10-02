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
