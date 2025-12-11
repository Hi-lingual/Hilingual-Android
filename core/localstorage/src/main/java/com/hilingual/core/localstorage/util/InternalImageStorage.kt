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
package com.hilingual.core.localstorage.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class InternalImageStorage @Inject constructor(@ApplicationContext private val context: Context) {
    suspend fun saveImageToInternal(imageUri: Uri): Uri? = withContext(Dispatchers.IO) {
        val directory = File(context.filesDir, "diary_images").apply { mkdirs() }
        val destFile = File(directory, "img_${UUID.randomUUID()}.jpg")

        try {
            copyImage(imageUri, destFile)
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", destFile)
        } catch (e: Exception) {
            Timber.tag("InternalImageStorage").e(e, "Failed to save image to internal storage")
            if (destFile.exists()) destFile.delete()
            null
        }
    }

    private fun copyImage(imageUri: Uri, destFile: File) {
        context.contentResolver.openInputStream(imageUri).use { input ->
            FileOutputStream(destFile).use { output ->
                input?.copyTo(output)
            }
        } ?: throw IOException()
    }

    suspend fun deleteImageFromInternal(imageUriString: String?) = withContext(Dispatchers.IO) {
        if (imageUriString == null) return@withContext

        val uri = imageUriString.toUri()
        val fileName = uri.lastPathSegment ?: return@withContext

        val file = File(context.filesDir, "diary_images/$fileName")
        if (file.exists()) {
            file.delete()
        }
    }
}
