package com.hilingual.core.localstorage.util

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
            context.contentResolver.openInputStream(imageUri).use { input ->
                FileOutputStream(destFile).use { output ->
                    input?.copyTo(output)
                }
            } ?: throw IOException()

            Uri.fromFile(destFile)
        } catch (e: Exception) {
            e.printStackTrace()
            if (destFile.exists()) destFile.delete()
            null
        }
    }

    suspend fun deleteImageFromInternal(imageUriString: String?) = withContext(Dispatchers.IO) {
        if (imageUriString == null) return@withContext

        val path = imageUriString.toUri().path ?: return@withContext

        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
    }
}
