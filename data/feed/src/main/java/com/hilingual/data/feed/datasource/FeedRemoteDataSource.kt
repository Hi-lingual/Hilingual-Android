package com.hilingual.data.feed.datasource

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.feed.dto.response.FeedProfileResponseDto

interface FeedRemoteDataSource {
    suspend fun getFeedProfile(
        targetUserId: Long
    ) : BaseResponse<FeedProfileResponseDto>
}
