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
package com.hilingual.presentation.feed

import androidx.compose.runtime.Immutable
import com.hilingual.core.common.util.UiState
import com.hilingual.data.feed.model.FeedListModel
import com.hilingual.presentation.feed.model.FeedItemUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

enum class FeedTab {
    RECOMMEND,
    FOLLOWING
}

@Immutable
internal data class FeedUiState(
    val myProfileUrl: String = "",
    val isRecommendRefreshing: Boolean = false,
    val isFollowingRefreshing: Boolean = false,
    val recommendFeedList: UiState<ImmutableList<FeedItemUiModel>> = UiState.Loading,
    val followingFeedList: UiState<ImmutableList<FeedItemUiModel>> = UiState.Loading,
    val hasFollowing: Boolean = true
) {
    companion object {
        val Fake = FeedUiState(
            recommendFeedList = UiState.Success(
                persistentListOf(
                    FeedItemUiModel(
                        userId = 1,
                        profileUrl = "https://avatars.githubusercontent.com/u/101113025?v=4",
                        nickname = "작나",
                        streak = 15,
                        sharedDateInMinutes = 5L,
                        content = "Just enjoyed a beautiful sunset over the mountains! #travel #nature",
                        imageUrl = "https://velog.velcdn.com/images/nahy-512/post/49cfbf3d-504f-4c2a-b0b9-458f287735e6/image.png",
                        diaryId = 1,
                        likeCount = 120,
                        isLiked = false,
                        isMine = true
                    ),
                    FeedItemUiModel(
                        userId = 2,
                        profileUrl = "",
                        nickname = "한민돌",
                        streak = 3,
                        sharedDateInMinutes = 60L * 2,
                        content = "Trying out a new recipe tonight. It's a bit spicy but delicious! 🌶️",
                        imageUrl = null,
                        diaryId = 2,
                        likeCount = 75,
                        isLiked = true,
                        isMine = false
                    ),
                    FeedItemUiModel(
                        userId = 3,
                        profileUrl = "",
                        nickname = "효비",
                        streak = 99,
                        sharedDateInMinutes = 60L * 24 * 3,
                        content = "Finished an amazing novel. Highly recommend it to anyone who loves a good mystery. " +
                            "The plot twists were incredible, and the characters were so well-developed. " +
                            "I couldn't put it down until I reached the very last page!",
                        imageUrl = "https://images.unsplash.com/photo-1532012197267-da84d127e765?q=80&w=774&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        diaryId = 3,
                        likeCount = 210,
                        isLiked = false,
                        isMine = false
                    )
                )
            ),
            followingFeedList = UiState.Success(
                persistentListOf(
                    FeedItemUiModel(
                        userId = 2,
                        profileUrl = "",
                        nickname = "한민돌",
                        streak = 3,
                        sharedDateInMinutes = 60L * 2,
                        content = "Trying out a new recipe tonight. It's a bit spicy but delicious! 🌶️",
                        imageUrl = null,
                        diaryId = 2,
                        likeCount = 75,
                        isLiked = true,
                        isMine = false
                    )
                )
            )
        )
    }
}

internal fun FeedListModel.toState(): ImmutableList<FeedItemUiModel> {
    return this.feedList.map {
        FeedItemUiModel(
            isMine = it.profile.isMine,
            userId = it.profile.userId,
            profileUrl = it.profile.profileImg,
            nickname = it.profile.nickname,
            streak = it.profile.streak,
            sharedDateInMinutes = it.diary.sharedDate,
            content = it.diary.originalText,
            imageUrl = it.diary.diaryImg,
            diaryId = it.diary.diaryId,
            likeCount = it.diary.likeCount,
            isLiked = it.diary.isLiked
        )
    }.toImmutableList()
}
