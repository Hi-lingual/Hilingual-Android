package com.hilingual.data.presigned.service

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Url

interface S3Service {
    @PUT
    suspend fun uploadFile(
        @Url uploadUrl: String,
        @Header("Content-Type") contentType: String,
        @Body file: RequestBody
    ): Response<Unit>
}
