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

import android.net.Uri
import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.core.common.util.toIsoDate
import com.hilingual.data.diary.datasource.DiaryRemoteDataSource
import com.hilingual.data.diary.model.BookmarkResult
import com.hilingual.data.diary.model.DiaryContentModel
import com.hilingual.data.diary.model.DiaryFeedbackCreateModel
import com.hilingual.data.diary.model.DiaryFeedbackModel
import com.hilingual.data.diary.model.DiaryRecommendExpressionModel
import com.hilingual.data.diary.model.PhraseBookmarkModel
import com.hilingual.data.diary.model.toDto
import com.hilingual.data.diary.model.toModel
import com.hilingual.data.diary.repository.DiaryRepository
import com.hilingual.data.presigned.repository.FileUploaderRepository
import java.time.LocalDate
import javax.inject.Inject

internal class DiaryRepositoryImpl @Inject constructor(
    private val diaryRemoteDataSource: DiaryRemoteDataSource,
    private val fileUploaderRepository: FileUploaderRepository
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
    ): Result<BookmarkResult> =
        suspendRunCatching {
            val response = diaryRemoteDataSource.patchPhraseBookmark(
                phraseId = phraseId,
                bookmarkRequestDto = bookmarkModel.toDto()
            )

            BookmarkResult.getOrError(response.code)
        }

    override suspend fun postDiaryFeedbackCreate(
        originalText: String,
        date: LocalDate,
        imageFileUri: Uri?
    ): Result<DiaryFeedbackCreateModel> = suspendRunCatching {
        val fileKey = if (imageFileUri != null) {
            fileUploaderRepository.uploadFile(
                uri = imageFileUri,
                purpose = "DIARY_IMAGE"
            ).getOrThrow()
        } else {
            null
        }

        diaryRemoteDataSource.postDiaryFeedbackCreate(
            originalText = originalText,
            date = date.toIsoDate(),
            fileKey = fileKey
        ).data!!.toModel()
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
}
