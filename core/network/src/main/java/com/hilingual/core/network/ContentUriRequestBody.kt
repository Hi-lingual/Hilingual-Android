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
package com.hilingual.core.network

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import android.util.Size
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import timber.log.Timber
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class ContentUriRequestBody @Inject constructor(
    @ApplicationContext context: Context,
    private val uri: Uri?,
    private val config: ImageConfig = ImageConfig.DEFAULT
) : RequestBody() {

    private val contentResolver = context.contentResolver
    private var compressedImage: ByteArray? = null
    private var metadata: ImageMetadata? = null

    private data class ImageMetadata(
        val size: Long,
        val mimeType: String?
    ) {
        companion object {
            val EMPTY = ImageMetadata(0L, null)
        }
    }

    data class ImageConfig(
        val maxWidth: Int,
        val maxHeight: Int,
        val maxFileSize: Int,
        val initialQuality: Int,
        val minQuality: Int,
        val format: Bitmap.CompressFormat
    ) {
        companion object {
            val DEFAULT = ImageConfig(
                maxWidth = 1920,
                maxHeight = 1920,
                maxFileSize = 2 * 1024 * 1024,
                initialQuality = 90,
                minQuality = 50,
                format = Bitmap.CompressFormat.JPEG
            )

            val WEBP = ImageConfig(
                maxWidth = 1920,
                maxHeight = 1920,
                maxFileSize = 2 * 1024 * 1024,
                initialQuality = 90,
                minQuality = 50,
                format = Bitmap.CompressFormat.WEBP_LOSSY
            )
        }
    }

    init {
        uri?.let {
            metadata = extractMetadata(it)
        }
    }

    @Volatile
    private var prepareImageJob: Deferred<Result<Unit>>? = null

    suspend fun prepareImage(): Result<Unit> {
        if (compressedImage != null) return Result.success(Unit)

        prepareImageJob?.let { return it.await() }

        return coroutineScope {
            val deferred = async(Dispatchers.IO) {
                if (compressedImage == null && uri != null) {
                    compressImage(uri).onSuccess { bytes ->
                        compressedImage = bytes
                    }
                }
                Result.success(Unit)
            }
            prepareImageJob = deferred
            deferred.await()
        }
    }

    private suspend fun compressImage(uri: Uri): Result<ByteArray> =
        withContext(Dispatchers.IO) {
            loadBitmap(uri).map { bitmap ->
                if ((metadata?.size ?: 0) <= config.maxFileSize) {
                    ByteArrayOutputStream().use { buffer ->
                        bitmap.compress(config.format, 100, buffer)
                        bitmap.recycle()
                        buffer.toByteArray()
                    }
                } else {
                    compressBitmap(bitmap).apply {
                        bitmap.recycle()
                    }
                }
            }
        }

    private suspend fun loadBitmap(uri: Uri): Result<Bitmap> =
        withContext(Dispatchers.IO) {
            runCatching {
                val source = ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
                    decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                    decoder.isMutableRequired = true
                    val size = calculateTargetSize(info.size.width, info.size.height)
                    decoder.setTargetSize(size.width, size.height)
                }
            }
        }

    private suspend fun compressBitmap(bitmap: Bitmap): ByteArray =
        withContext(Dispatchers.Default) {
            val estimatedSize = max(32 * 1024, min(bitmap.byteCount / 4, config.maxFileSize))
            ByteArrayOutputStream(estimatedSize).use { buffer ->
                var lowerQuality = config.minQuality
                var upperQuality = config.initialQuality
                var bestQuality = lowerQuality

                while (lowerQuality <= upperQuality) {
                    val midQuality = (lowerQuality + upperQuality) / 2
                    buffer.reset()

                    if (config.format == Bitmap.CompressFormat.PNG) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, buffer)
                    } else {
                        bitmap.compress(config.format, midQuality, buffer)
                    }

                    if (buffer.size() <= config.maxFileSize) {
                        bestQuality = midQuality
                        lowerQuality = midQuality + 1
                    } else {
                        upperQuality = midQuality - 1
                    }
                }

                Timber.d("Compression completed - Quality: $bestQuality, Size: ${buffer.size()} bytes")
                return@use buffer.toByteArray()
            }
        }

    private fun extractMetadata(uri: Uri): ImageMetadata =
        runCatching {
            contentResolver.query(
                uri,
                arrayOf(
                    MediaStore.Images.Media.SIZE,
                    MediaStore.Images.Media.MIME_TYPE
                ),
                null,
                null,
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    ImageMetadata(
                        size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)),
                        mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE))
                    )
                } else {
                    ImageMetadata.EMPTY
                }
            } ?: ImageMetadata.EMPTY
        }.getOrDefault(ImageMetadata.EMPTY)

    private fun calculateTargetSize(width: Int, height: Int): Size {
        if (width <= config.maxWidth && height <= config.maxHeight) {
            return Size(width, height)
        }
        val scaleFactor = min(config.maxWidth / width.toFloat(), config.maxHeight / height.toFloat())
        val targetWidth = (width * scaleFactor).toInt()
        val targetHeight = (height * scaleFactor).toInt()
        return Size(targetWidth, targetHeight)
    }

    override fun contentLength(): Long = compressedImage?.size?.toLong() ?: -1L

    override fun contentType(): MediaType? = metadata?.mimeType?.toMediaTypeOrNull()

    override fun writeTo(sink: BufferedSink) {
        compressedImage?.let(sink::write)
    }
}
