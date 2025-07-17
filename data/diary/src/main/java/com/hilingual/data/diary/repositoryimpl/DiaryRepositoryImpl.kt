package com.hilingual.data.diary.repositoryimpl

import android.content.ContentResolver
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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

internal class DiaryRepositoryImpl @Inject constructor(
    private val contentResolver: ContentResolver,
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
    ): Result<DiaryFeedbackCreateModel> {
        val originalTextToRequestBody = originalText.toRequestBody(APPLICATION_JSON.toMediaType())

        val dateToString = date.format(DATE_FORMATTER)
        val dateToRequestBody = dateToString.toRequestBody(APPLICATION_JSON.toMediaType())

        val imageFile = if (imageFileUri != null) {
            ContentUriRequestBody(
                contentResolver = contentResolver,
                uri = imageFileUri
            ).toFormData(name = IMAGE_FILE_NAME)
        } else {
            null
        }

        return suspendRunCatching {
            diaryRemoteDataSource.postDiaryFeedbackCreate(
                originalText = originalTextToRequestBody,
                date = dateToRequestBody,
                imageFile = imageFile
            ).data!!.toModel()
        }
    }

    companion object {
        const val APPLICATION_JSON = "application/json"
        const val IMAGE_FILE_NAME = "imageFile"
    }

    private val DATE_FORMATTER: DateTimeFormatter =
        DateTimeFormatter.ISO_LOCAL_DATE
}
