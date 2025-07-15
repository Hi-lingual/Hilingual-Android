package com.hilingual.data.diary.datasourceimpl

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.diary.datasource.DiaryRemoteDataSource
import com.hilingual.data.diary.dto.request.BookmarkRequestDto
import com.hilingual.data.diary.dto.response.DiaryContentResponseDto
import com.hilingual.data.diary.dto.response.DiaryFeedbackResponseDto
import com.hilingual.data.diary.dto.response.DiaryRecommendExpressionResponseDto
import com.hilingual.data.diary.service.DiaryService
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
}
