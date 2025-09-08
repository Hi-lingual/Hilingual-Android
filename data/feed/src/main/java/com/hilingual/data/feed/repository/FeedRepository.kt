package com.hilingual.data.feed.repository

import com.hilingual.data.feed.model.FeedDiaryProfileModel

interface FeedRepository {
    suspend fun getFeedDiaryProfile(diaryId: Long): Result<FeedDiaryProfileModel>

    suspend fun postIsLike(diaryId: Long, isLiked: Boolean): Result<Unit>
}
