package com.hilingual.data.feed.datasource

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.feed.dto.response.FeedResponseDto

interface FeedRemoteDataSource {
    suspend fun getRecommendFeeds(): BaseResponse<FeedResponseDto>
}
