/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.data.feed.service

import com.hilingual.core.network.model.BaseResponse
import com.hilingual.data.feed.dto.request.LikeRequestDto
import com.hilingual.data.feed.dto.response.DiaryProfileResponseDto
import com.hilingual.data.feed.dto.response.FeedProfileResponseDto
import com.hilingual.data.feed.dto.response.FollowingFeedResponseDto
import com.hilingual.data.feed.dto.response.LikedDiariesResponseDto
import com.hilingual.data.feed.dto.response.RecommendFeedResponseDto
import com.hilingual.data.feed.dto.response.SharedDiariesResponseDto
import com.hilingual.data.feed.dto.response.UserResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FeedService {
    @GET("/api/v1/feed/profiles/{targetUserId}")
    suspend fun getFeedProfile(
        @Path("targetUserId") targetUserId: Long
    ): BaseResponse<FeedProfileResponseDto>

    @GET("/api/v1/feed/profiles/{targetUserId}/diaries/shared")
    suspend fun getSharedDiaries(
        @Path("targetUserId") targetUserId: Long
    ): BaseResponse<SharedDiariesResponseDto>

    @GET("/api/v1/feed/profiles/{targetUserId}/diaries/liked")
    suspend fun getLikedDiaries(
        @Path("targetUserId") targetUserId: Long
    ): BaseResponse<LikedDiariesResponseDto>

    @GET("/api/v1/feed/recommend")
    suspend fun getRecommendFeeds(): BaseResponse<RecommendFeedResponseDto>

    @GET("/api/v1/feed/following")
    suspend fun getFollowingFeeds(): BaseResponse<FollowingFeedResponseDto>

    @GET("/api/v1/feed/{diaryId}/users/profiles")
    suspend fun getFeedDiaryProfile(
        @Path("diaryId") diaryId: Long
    ): BaseResponse<DiaryProfileResponseDto>

    @POST("/api/v1/feed/likes/{diaryId}")
    suspend fun postIsLike(
        @Path("diaryId") diaryId: Long,
        @Body likeRequestDto: LikeRequestDto
    ): BaseResponse<Unit>

    @GET("/api/v1/feed/search")
    suspend fun getUserSearchResult(
        @Query("keyword") keyword: String
    ): BaseResponse<UserResponseDto>
}
