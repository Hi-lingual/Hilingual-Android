package com.hilingual.data.feed.repository

import com.hilingual.data.feed.model.FeedListModel

interface FeedRepository {
    suspend fun getRecommendFeeds(): Result<FeedListModel>

    suspend fun getFollowingFeeds(): Result<FeedListModel>
}
