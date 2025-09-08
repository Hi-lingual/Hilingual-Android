package com.hilingual.data.feed.repository

import com.hilingual.data.feed.model.FeedDiaryProfileModel
import com.hilingual.data.feed.model.FeedListModel

interface FeedRepository {
    suspend fun getRecommendFeeds(): Result<FeedListModel>

    suspend fun getFollowingFeeds(): Result<FeedListModel>

    suspend fun getFeedDiaryProfile(diaryId: Long): Result<FeedDiaryProfileModel>

    suspend fun postIsLike(diaryId: Long, isLiked: Boolean): Result<Unit>
}
