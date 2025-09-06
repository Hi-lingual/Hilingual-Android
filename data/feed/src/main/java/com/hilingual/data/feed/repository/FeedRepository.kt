package com.hilingual.data.feed.repository

import com.hilingual.data.feed.model.FeedProfileModel
import com.hilingual.data.feed.model.SharedDiariesModel

interface FeedRepository {
    suspend fun getFeedProfile(
        targetUserId: Long
    ) : Result<FeedProfileModel>

    suspend fun getSharedDiaries(
        targetUserId: Long
    ) : Result<SharedDiariesModel>
}