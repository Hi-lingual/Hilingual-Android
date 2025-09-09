package com.hilingual.data.feed.repository

import com.hilingual.data.feed.model.FeedDiaryProfileModel
import com.hilingual.data.feed.model.FeedListModel
import com.hilingual.data.feed.model.FeedProfileModel
import com.hilingual.data.feed.model.LikedDiariesModel
import com.hilingual.data.feed.model.SharedDiariesModel
import com.hilingual.data.feed.model.UserListModel

interface FeedRepository {
    suspend fun getFeedProfile(
        targetUserId: Long
    ): Result<FeedProfileModel>

    suspend fun getSharedDiaries(
        targetUserId: Long
    ): Result<SharedDiariesModel>

    suspend fun getLikedDiaries(
        targetUserId: Long
    ): Result<LikedDiariesModel>

    suspend fun getRecommendFeeds(): Result<FeedListModel>

    suspend fun getFollowingFeeds(): Result<FeedListModel>

    suspend fun getFeedDiaryProfile(diaryId: Long): Result<FeedDiaryProfileModel>

    suspend fun postIsLike(diaryId: Long, isLiked: Boolean): Result<Unit>

    suspend fun getUserSearchResult(keyword: String): Result<UserListModel>
}
