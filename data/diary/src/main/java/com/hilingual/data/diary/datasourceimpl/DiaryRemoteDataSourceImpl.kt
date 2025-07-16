package com.hilingual.data.diary.datasourceimpl

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import com.hilingual.core.network.BaseResponse
import com.hilingual.data.diary.datasource.DiaryRemoteDataSource
import com.hilingual.data.diary.dto.request.BookmarkRequestDto
import com.hilingual.data.diary.dto.response.DiaryContentResponseDto
import com.hilingual.data.diary.dto.response.DiaryFeedbackCreateResponseDto
import com.hilingual.data.diary.dto.response.DiaryFeedbackResponseDto
import com.hilingual.data.diary.dto.response.DiaryRecommendExpressionResponseDto
import com.hilingual.data.diary.service.DiaryService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

internal class DiaryRemoteDataSourceImpl @Inject constructor(
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

    override suspend fun postDiaryFeedbackCreate(
        originalText: RequestBody,
        date: RequestBody,
        imageFile: MultipartBody.Part?
    ): BaseResponse<DiaryFeedbackCreateResponseDto> =
        diaryService.postDiaryFeedbackCreate(
            originalText = originalText,
            date = date,
            imageFile = imageFile
        )
}
