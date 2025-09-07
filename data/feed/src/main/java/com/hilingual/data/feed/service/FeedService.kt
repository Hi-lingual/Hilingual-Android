package com.hilingual.data.feed.service

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.feed.dto.response.UserResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

internal interface FeedService {
    @GET("/api/v1/feed/search")
    suspend fun getUserSearchResult(
        @Query("keyword") keyword: String
    ): BaseResponse<UserResponseDto>
}
