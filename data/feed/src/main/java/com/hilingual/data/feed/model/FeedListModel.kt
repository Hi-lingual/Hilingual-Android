package com.hilingual.data.feed.model

import com.hilingual.data.feed.dto.response.FollowingFeedResponseDto
import com.hilingual.data.feed.dto.response.RecommendFeedResponseDto

data class FeedListModel(
    val feedList: List<FeedItemModel>,
    val hasFollowing: Boolean = true
)

internal fun RecommendFeedResponseDto.toModel(): FeedListModel = FeedListModel(
    feedList = this.diaryList.map { it.toModel() }
)

internal fun FollowingFeedResponseDto.toModel(): FeedListModel = FeedListModel(
    feedList = this.diaryList.map { it.toModel() },
    hasFollowing = this.haveFollowing
)
