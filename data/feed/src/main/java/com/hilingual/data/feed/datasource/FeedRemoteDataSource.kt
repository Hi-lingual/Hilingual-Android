package com.hilingual.data.feed.datasource

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.feed.dto.request.LikeRequestDto
import com.hilingual.data.feed.dto.response.DiaryProfileResponseDto

interface FeedRemoteDataSource {
    suspend fun getFeedDiaryProfile(
        diaryId: Long
    ): BaseResponse<DiaryProfileResponseDto>

    suspend fun postIsLike(
        diaryId: Long,
        likeRequestDto: LikeRequestDto
    ): BaseResponse<Unit>
}
