package com.hilingual.presentation.feed

import androidx.compose.runtime.Immutable
import com.hilingual.presentation.feed.model.FeedListItemUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class FeedUiState(
    val myProfileUrl: String = "",
    val recommendFeedList: ImmutableList<FeedListItemUiModel> = persistentListOf(),
    val followingFeedList: ImmutableList<FeedListItemUiModel> = persistentListOf(),
    val hasFollowing: Boolean = true
) {
    companion object {
        val Fake = FeedUiState(
            myProfileUrl = "https://avatars.githubusercontent.com/u/101113025?v=4",
            recommendFeedList = persistentListOf(
                FeedListItemUiModel(
                    userId = 101L,
                    profileUrl = "https://avatars.githubusercontent.com/u/101113025?v=4",
                    nickname = "가짜유저",
                    streak = 7,
                    sharedDateInMinutes = 20,
                    content = "가짜 피드에서 온 데이터!",
                    imageUrl = null,
                    diaryId = 1L,
                    likeCount = 5,
                    isLiked = false
                )
            ),
            followingFeedList = persistentListOf(),
            hasFollowing = false
        )
    }
}
