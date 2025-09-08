package com.hilingual.data.feed.repository

import com.hilingual.data.feed.model.FeedProfileModel
import com.hilingual.data.feed.model.LikedDiariesModel
import com.hilingual.data.feed.model.SharedDiariesModel
import com.hilingual.data.feed.model.FeedDiaryProfileModel

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

    suspend fun getFeedDiaryProfile(diaryId: Long): Result<FeedDiaryProfileModel>

    suspend fun postIsLike(diaryId: Long, isLiked: Boolean): Result<Unit>
}
