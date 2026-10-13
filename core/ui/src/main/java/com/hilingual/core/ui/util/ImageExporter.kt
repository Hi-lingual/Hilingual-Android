package com.hilingual.core.ui.util

import android.content.ClipData
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.util.suspendRunCatching
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val GALLERY_DIRECTORY = "Hilingual"
private const val SHARE_DIRECTORY = "shared_images"

@Stable
class ImageExporter internal constructor(
    private val context: Context,
    internal val graphicsLayer: GraphicsLayer,
    private val coroutineScope: CoroutineScope,
) {
    private var isExporting = false

    fun share(fileName: String) = export { bitmap ->
        context.shareImage(bitmap, fileName)
    }

    fun saveToGallery(fileName: String, onSuccess: () -> Unit) = export { bitmap ->
        context.saveImageToGallery(bitmap, fileName)
        onSuccess()
    }

    private fun export(block: suspend (Bitmap) -> Unit) {
        if (isExporting) return
        isExporting = true
        coroutineScope.launch {
            suspendRunCatching {
                block(graphicsLayer.toImageBitmap().asAndroidBitmap())
            }.onLogFailure { }
            isExporting = false
        }
    }
}

fun Modifier.recordForExport(exporter: ImageExporter): Modifier = drawWithContent {
    exporter.graphicsLayer.record { this@drawWithContent.drawContent() }
}

@Composable
fun rememberImageExporter(): ImageExporter {
    val context = LocalContext.current
    val graphicsLayer = rememberGraphicsLayer()
    val coroutineScope = rememberCoroutineScope()
    return remember(context, graphicsLayer, coroutineScope) {
        ImageExporter(context, graphicsLayer, coroutineScope)
    }
}

private suspend fun Context.saveImageToGallery(bitmap: Bitmap, fileName: String) = withContext(Dispatchers.IO) {
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

private suspend fun Context.shareImage(bitmap: Bitmap, fileName: String) {
    val uri = withContext(Dispatchers.IO) {
        val directory = File(cacheDir, SHARE_DIRECTORY).apply {
            deleteRecursively()
            mkdirs()
        }
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
