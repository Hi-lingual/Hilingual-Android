package com.hilingual.data.feed.datasource

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.feed.dto.response.UserResponseDto

interface FeedRemoteDataSource {
    suspend fun getUserSearchResult(keyword: String): BaseResponse<UserResponseDto>
}
