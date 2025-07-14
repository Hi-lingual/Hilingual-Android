package com.hilingual.data.diary.datasource

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.diary.dto.response.DiaryContentResponseDto

interface DiaryRemoteDataSource {
    suspend fun getDiaryContent(
        diaryId: Long
    ): BaseResponse<DiaryContentResponseDto>
}
