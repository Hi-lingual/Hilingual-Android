package com.hilingual.data.diary.repository

import android.net.Uri

interface TextRecognitionRepository {
    suspend fun extractTextFromImage(uri: Uri): Result<String>
}
