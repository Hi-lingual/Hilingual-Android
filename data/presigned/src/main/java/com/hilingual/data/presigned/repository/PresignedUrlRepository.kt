package com.hilingual.data.presigned.repository

import com.hilingual.data.presigned.model.PresignedUrlModel

interface PresignedUrlRepository {
    suspend fun getPresignedUrl(purpose: String, contentType: String): Result<PresignedUrlModel>
}
