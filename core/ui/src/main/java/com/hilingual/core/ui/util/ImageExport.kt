package com.hilingual.core.ui.util

import android.content.ClipData
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val GALLERY_DIRECTORY = "Hilingual"
private const val SHARE_DIRECTORY = "shared_images"

suspend fun Context.saveImageToGallery(bitmap: Bitmap, fileName: String) = withContext(Dispatchers.IO) {
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.png")
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/$GALLERY_DIRECTORY")
        put(MediaStore.Images.Media.IS_PENDING, 1)
    }
    val uri = checkNotNull(contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)) {
        "Failed to create MediaStore entry"
    }

    runCatching {
        checkNotNull(contentResolver.openOutputStream(uri)) { "Failed to open $uri" }.use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
        values.clear()
        values.put(MediaStore.Images.Media.IS_PENDING, 0)
        contentResolver.update(uri, values, null, null)
    }.onFailure {
        contentResolver.delete(uri, null, null)
        throw it
    }
}

suspend fun Context.shareImage(bitmap: Bitmap, fileName: String) {
    val uri = withContext(Dispatchers.IO) {
        val directory = File(cacheDir, SHARE_DIRECTORY).apply { mkdirs() }
        val file = File(directory, "$fileName.png")
        file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        FileProvider.getUriForFile(this@shareImage, "$packageName.fileprovider", file)
    }

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        clipData = ClipData.newRawUri(null, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    startActivity(Intent.createChooser(sendIntent, null))
}
