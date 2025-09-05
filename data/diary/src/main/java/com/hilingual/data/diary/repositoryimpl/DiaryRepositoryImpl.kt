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
package com.hilingual.data.diary.repositoryimpl

import android.content.Context
import android.net.Uri
import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.core.network.ContentUriRequestBody
import com.hilingual.data.diary.datasource.DiaryRemoteDataSource
import com.hilingual.data.diary.model.DiaryContentModel
import com.hilingual.data.diary.model.DiaryFeedbackCreateModel
import com.hilingual.data.diary.model.DiaryFeedbackModel
import com.hilingual.data.diary.model.DiaryRecommendExpressionModel
import com.hilingual.data.diary.model.PhraseBookmarkModel
import com.hilingual.data.diary.model.toDto
import com.hilingual.data.diary.model.toModel
import com.hilingual.data.diary.repository.DiaryRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

internal class DiaryRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val diaryRemoteDataSource: DiaryRemoteDataSource
) : DiaryRepository {
    override suspend fun getDiaryContent(diaryId: Long): Result<DiaryContentModel> =
        suspendRunCatching {
            diaryRemoteDataSource.getDiaryContent(diaryId).data!!.toModel()
        }

    override suspend fun getDiaryFeedbacks(diaryId: Long): Result<List<DiaryFeedbackModel>> =
        suspendRunCatching {
            diaryRemoteDataSource.getDiaryFeedbacks(diaryId).data!!.feedbackList.map { it.toModel() }
        }

    override suspend fun getDiaryRecommendExpressions(diaryId: Long): Result<List<DiaryRecommendExpressionModel>> =
        suspendRunCatching {
            diaryRemoteDataSource.getDiaryRecommendExpressions(diaryId).data!!.phraseList.map { it.toModel() }
        }

    override suspend fun patchPhraseBookmark(
        phraseId: Long,
        bookmarkModel: PhraseBookmarkModel
    ): Result<Unit> =
        suspendRunCatching {
            diaryRemoteDataSource.patchPhraseBookmark(
                phraseId = phraseId,
                bookmarkRequestDto = bookmarkModel.toDto()
            )
        }

    override suspend fun postDiaryFeedbackCreate(
        originalText: String,
        date: LocalDate,
        imageFileUri: Uri?
    ): Result<DiaryFeedbackCreateModel> = suspendRunCatching {
        coroutineScope {
            val originalTextRequestBody = originalText.toRequestBody(APPLICATION_JSON.toMediaType())
            val dateRequestBody = date.format(DATE_FORMATTER).toRequestBody(APPLICATION_JSON.toMediaType())

            val imagePart = imageFileUri?.let {
                async {
                    val requestBody = ContentUriRequestBody(context, it)
                    requestBody.prepareImage()
                    requestBody.toFormData(IMAGE_FILE_NAME)
                }
            }?.await()

            diaryRemoteDataSource.postDiaryFeedbackCreate(
                originalText = originalTextRequestBody,
                date = dateRequestBody,
                imageFile = imagePart
            ).data!!.toModel()
        }
    }

    override suspend fun patchDiaryPublish(diaryId: Long): Result<Unit> =
        suspendRunCatching {
            diaryRemoteDataSource.patchDiaryPublish(diaryId)
        }

    override suspend fun patchDiaryUnpublish(diaryId: Long): Result<Unit> =
        suspendRunCatching {
            diaryRemoteDataSource.patchDiaryUnpublish(diaryId)
        }

    override suspend fun deleteDiary(diaryId: Long): Result<Unit> =
        suspendRunCatching {
            diaryRemoteDataSource.deleteDiary(diaryId)
        }

    companion object {
        const val APPLICATION_JSON = "application/json"
        const val IMAGE_FILE_NAME = "imageFile"

        val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    }
}
