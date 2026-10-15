package com.hilingual.core.ui.util

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.util.suspendRunCatching
import java.io.File
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val GALLERY_DIRECTORY = "Hilingual"
private const val SHARE_DIRECTORY = "shared_images"

class ImageExporter internal constructor(
    private val context: Context,
    private val graphicsLayer: GraphicsLayer,
    private val coroutineScope: CoroutineScope,
) {
    private var isExporting = false
    private var shareTargetReceiver: BroadcastReceiver? = null

    val recordModifier: Modifier = Modifier.drawWithContent {
        graphicsLayer.record { this@drawWithContent.drawContent() }
    }

    fun share(
        fileName: String,
        onTargetChosen: (() -> Unit)? = null,
        onFailure: () -> Unit = {},
    ) = export(
        onFailure = {
            unregisterShareTargetReceiver()
            onFailure()
        },
    ) { bitmap ->
        val targetChosenAction = onTargetChosen?.let(::registerShareTargetReceiver)
        context.shareImage(bitmap, fileName, targetChosenAction)
    }

    fun saveToGallery(
        fileName: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit = {},
    ) = export(onFailure) { bitmap ->
        context.saveImageToGallery(bitmap, fileName)
        onSuccess()
    }

    internal fun release() = unregisterShareTargetReceiver()

    private fun export(onFailure: () -> Unit, block: suspend (Bitmap) -> Unit) {
        if (isExporting) return
        isExporting = true
        coroutineScope.launch {
            suspendRunCatching {
                block(graphicsLayer.toImageBitmap().asAndroidBitmap())
            }.onLogFailure { onFailure() }
            isExporting = false
        }
    }

    private fun registerShareTargetReceiver(onTargetChosen: () -> Unit): String {
        unregisterShareTargetReceiver()
        val action = "${context.packageName}.IMAGE_SHARE_TARGET_CHOSEN.${UUID.randomUUID()}"
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                unregisterShareTargetReceiver()
                onTargetChosen()
            }
        }
        ContextCompat.registerReceiver(
            context,
            receiver,
            IntentFilter(action),
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
        shareTargetReceiver = receiver
        return action
    }

    private fun unregisterShareTargetReceiver() {
        shareTargetReceiver?.let(context::unregisterReceiver)
        shareTargetReceiver = null
    }
}

@Composable
fun rememberImageExporter(): ImageExporter {
    val context = LocalContext.current
    val graphicsLayer = rememberGraphicsLayer()
    val coroutineScope = rememberCoroutineScope()
    val exporter = remember(context, graphicsLayer, coroutineScope) {
        ImageExporter(context, graphicsLayer, coroutineScope)
    }

    DisposableEffect(exporter) {
        onDispose { exporter.release() }
    }

    return exporter
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
            check(bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)) { "Failed to compress image" }
        }
        values.clear()
        values.put(MediaStore.Images.Media.IS_PENDING, 0)
        check(contentResolver.update(uri, values, null, null) > 0) { "Failed to publish $uri" }
    }.onFailure {
        contentResolver.delete(uri, null, null)
        throw it
    }
}

private suspend fun Context.shareImage(bitmap: Bitmap, fileName: String, targetChosenAction: String?) {
    val uri = withContext(Dispatchers.IO) {
        val directory = File(cacheDir, SHARE_DIRECTORY).apply {
            deleteRecursively()
            mkdirs()
        }
        val file = File(directory, "$fileName.png")
        file.outputStream().use {
            check(bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)) { "Failed to compress image" }
        }
        FileProvider.getUriForFile(this@shareImage, "$packageName.fileprovider", file)
    }

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        clipData = ClipData.newRawUri(null, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    val targetChosenSender = targetChosenAction?.let {
        PendingIntent.getBroadcast(
            this,
            0,
            Intent(it).setPackage(packageName),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        ).intentSender
    }
    startActivity(Intent.createChooser(sendIntent, null, targetChosenSender))
}
