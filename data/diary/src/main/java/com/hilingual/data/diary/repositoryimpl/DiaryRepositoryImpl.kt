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
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
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
        var imageFile: MultipartBody.Part? = null

        val originalTextToRequestBody =
            Json.encodeToString(originalText).toRequestBody("application/json".toMediaType())

        val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
        val dateToString = date.format(dateFormatter)
        val dateToRequestBody =
            Json.encodeToString(dateToString).toRequestBody("application/json".toMediaType())

        if (imageFileUri != null) {
            imageFile = ContentUriRequestBody(
                contentResolver = contentResolver,
                uri = imageFileUri
            ).toFormData(name = IMAGE_FILE_NAME)
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
        const val IMAGE_FILE_NAME = "imageFile"
    }
}
