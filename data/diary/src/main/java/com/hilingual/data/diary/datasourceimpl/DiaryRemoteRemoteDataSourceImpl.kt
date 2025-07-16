package com.hilingual.data.diary.datasourceimpl

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.diary.datasource.DiaryRemoteDataSource
import com.hilingual.data.diary.dto.request.BookmarkRequestDto
import com.hilingual.data.diary.dto.response.DiaryContentResponseDto
import com.hilingual.data.diary.dto.response.DiaryFeedbackCreateResposeDto
import com.hilingual.data.diary.dto.response.DiaryFeedbackResponseDto
import com.hilingual.data.diary.dto.response.DiaryRecommendExpressionResponseDto
import com.hilingual.data.diary.service.DiaryService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

internal class DiaryRemoteRemoteDataSourceImpl @Inject constructor(
    private val diaryService: DiaryService
) : DiaryRemoteDataSource {
    override suspend fun getDiaryContent(diaryId: Long): BaseResponse<DiaryContentResponseDto> =
        diaryService.getDiaryContent(diaryId)

    override suspend fun getDiaryFeedbacks(diaryId: Long): BaseResponse<DiaryFeedbackResponseDto> =
        diaryService.getDiaryFeedbacks(diaryId)

    override suspend fun getDiaryRecommendExpressions(diaryId: Long): BaseResponse<DiaryRecommendExpressionResponseDto> =
        diaryService.getDiaryRecommendExpressions(diaryId)

    override suspend fun patchPhraseBookmark(
        phraseId: Long,
        bookmarkRequestDto: BookmarkRequestDto
    ): BaseResponse<Unit> =
        diaryService.patchPhraseBookmark(
            phraseId = phraseId,
            bookmarkRequestDto = bookmarkRequestDto
        )

    override suspend fun getDiaryFeedbackCreate(
        originalText: String,
        date: String,
        imageFile: File?
    ): BaseResponse<DiaryFeedbackCreateResposeDto> {
        val originalTextPart = originalText.toRequestBody("text/plain".toMediaType())
        val datePart = date.toRequestBody("text/plain".toMediaType())

        val imagePart = imageFile?.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("imageFile", it.name, requestFile)
        }

        return diaryService.postDiaryFeedbackCreate(
            originalText = originalTextPart,
            date = datePart,
            imageFile = imagePart
        )
    }
}
