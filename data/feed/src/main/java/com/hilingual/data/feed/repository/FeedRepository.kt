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
