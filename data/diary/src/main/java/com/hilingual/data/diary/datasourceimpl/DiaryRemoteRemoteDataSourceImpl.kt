package com.hilingual.data.diary.datasourceimpl

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.diary.datasource.DiaryRemoteDataSource
import com.hilingual.data.diary.dto.response.DiaryContentResponseDto
import com.hilingual.data.diary.service.DiaryService
import javax.inject.Inject

internal class DiaryRemoteRemoteDataSourceImpl @Inject constructor(
    private val diaryService: DiaryService
) : DiaryRemoteDataSource {
    override suspend fun getDiaryContent(diaryId: Long): BaseResponse<DiaryContentResponseDto> =
        diaryService.getDiaryContent(diaryId)
}
