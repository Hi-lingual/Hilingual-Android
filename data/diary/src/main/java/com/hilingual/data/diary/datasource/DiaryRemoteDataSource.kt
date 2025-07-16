package com.hilingual.data.diary.datasource

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.diary.dto.request.BookmarkRequestDto
import com.hilingual.data.diary.dto.response.DiaryContentResponseDto
import com.hilingual.data.diary.dto.response.DiaryFeedbackCreateResponseDto
import com.hilingual.data.diary.dto.response.DiaryFeedbackResponseDto
import com.hilingual.data.diary.dto.response.DiaryRecommendExpressionResponseDto
import java.io.File

interface DiaryRemoteDataSource {
    suspend fun getDiaryContent(
        diaryId: Long
    ): BaseResponse<DiaryContentResponseDto>

    suspend fun getDiaryFeedbacks(
        diaryId: Long
    ): BaseResponse<DiaryFeedbackResponseDto>

    suspend fun getDiaryRecommendExpressions(
        diaryId: Long
    ): BaseResponse<DiaryRecommendExpressionResponseDto>

    suspend fun patchPhraseBookmark(
        phraseId: Long,
        bookmarkRequestDto: BookmarkRequestDto
    ): BaseResponse<Unit>

    suspend fun postDiaryFeedbackCreate(
        originalText: String,
        date: String,
        imageFile: File? = null
    ): BaseResponse<DiaryFeedbackCreateResponseDto>
}
