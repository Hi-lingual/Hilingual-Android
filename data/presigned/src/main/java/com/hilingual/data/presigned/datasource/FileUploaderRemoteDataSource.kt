package com.hilingual.data.presigned.datasource

import okhttp3.RequestBody
import retrofit2.Response

interface FileUploaderRemoteDataSource {
    suspend fun uploadFile(
        uploadUrl: String,
        contentType: String,
        file: RequestBody
    ): Response<Unit>
}
