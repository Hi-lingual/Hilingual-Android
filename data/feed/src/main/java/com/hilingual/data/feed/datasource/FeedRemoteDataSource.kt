package com.hilingual.data.feed.datasource

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.feed.dto.response.FeedProfileResponseDto
import com.hilingual.data.feed.dto.response.SharedDiariesResponseDto

interface FeedRemoteDataSource {
    suspend fun getFeedProfile(
        targetUserId: Long
    ) : BaseResponse<FeedProfileResponseDto>

    suspend fun getSharedDiaries(
        targetUserId: Long
    ) : BaseResponse<SharedDiariesResponseDto>
}
