/*
 * Copyright 2026 The Hilingual Project
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
package com.hilingual.data.diary.repositoryimpl

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import com.hilingual.data.diary.repository.TextRecognitionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TextRecognitionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val recognizer: TextRecognizer,
) : TextRecognitionRepository {

    override suspend fun extractTextFromImage(uri: Uri): Result<String> =
        withContext(Dispatchers.IO) {
            runCatching {
                val image = InputImage.fromFilePath(context, uri)
                recognizer.process(image).await().text
            }
        }
}
