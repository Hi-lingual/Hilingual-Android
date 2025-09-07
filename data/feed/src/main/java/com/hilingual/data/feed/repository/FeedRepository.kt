package com.hilingual.data.feed.repository

import com.hilingual.data.feed.model.UserListModel

interface FeedRepository {
    suspend fun getUserSearchResult(keyword: String): Result<UserListModel>
}
