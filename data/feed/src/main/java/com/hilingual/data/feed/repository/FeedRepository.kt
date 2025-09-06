package com.hilingual.data.feed.repository

import com.hilingual.data.feed.model.FeedProfileModel

interface FeedRepository {
    suspend fun getFeedProfile(
        targetUserId: Long
    ) : Result<FeedProfileModel>
}