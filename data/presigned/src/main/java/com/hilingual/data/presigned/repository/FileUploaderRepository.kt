package com.hilingual.data.presigned.repository

import android.net.Uri

interface FileUploaderRepository {
    suspend fun uploadFile(
        uri: Uri,
        purpose: String
    ): Result<String>
}
