package com.hilingual.data.feed.datasource

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.feed.dto.request.LikeRequestDto
import com.hilingual.data.feed.dto.response.DiaryProfileResponseDto
import com.hilingual.data.feed.dto.response.FeedProfileResponseDto
import com.hilingual.data.feed.dto.response.LikedDiariesResponseDto
import com.hilingual.data.feed.dto.response.SharedDiariesResponseDto
import com.hilingual.data.feed.dto.response.FollowingFeedResponseDto
import com.hilingual.data.feed.dto.response.RecommendFeedResponseDto

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
}
