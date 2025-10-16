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
package com.hilingual.data.feed.datasource

import com.hilingual.core.network.model.BaseResponse
import com.hilingual.data.feed.dto.request.LikeRequestDto
import com.hilingual.data.feed.dto.response.DiaryProfileResponseDto
import com.hilingual.data.feed.dto.response.FeedProfileResponseDto
import com.hilingual.data.feed.dto.response.FollowingFeedResponseDto
import com.hilingual.data.feed.dto.response.LikedDiariesResponseDto
import com.hilingual.data.feed.dto.response.RecommendFeedResponseDto
import com.hilingual.data.feed.dto.response.SharedDiariesResponseDto
import com.hilingual.data.feed.dto.response.UserResponseDto

interface FeedRemoteDataSource {
    suspend fun getFeedProfile(
        targetUserId: Long
    ): BaseResponse<FeedProfileResponseDto>

    suspend fun getSharedDiaries(
        targetUserId: Long
    ): BaseResponse<SharedDiariesResponseDto>

    suspend fun getLikedDiaries(
        targetUserId: Long
    ): BaseResponse<LikedDiariesResponseDto>

    suspend fun getRecommendFeeds(): BaseResponse<RecommendFeedResponseDto>

    suspend fun getFollowingFeeds(): BaseResponse<FollowingFeedResponseDto>

    suspend fun getFeedDiaryProfile(
        diaryId: Long
    ): BaseResponse<DiaryProfileResponseDto>

    suspend fun postIsLike(
        diaryId: Long,
        likeRequestDto: LikeRequestDto
    ): BaseResponse<Unit>

    suspend fun getUserSearchResult(keyword: String): BaseResponse<UserResponseDto>
}
