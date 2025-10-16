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
package com.hilingual.presentation.feedprofile.follow

import androidx.compose.runtime.Immutable
import com.hilingual.core.common.util.UiState
import com.hilingual.data.user.model.follow.FollowState
import com.hilingual.presentation.feedprofile.follow.model.FollowItemModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class FollowListUiState(
    val followerList: UiState<ImmutableList<FollowItemModel>> = UiState.Loading,
    val followingList: UiState<ImmutableList<FollowItemModel>> = UiState.Loading,
    val isFollowerRefreshing: Boolean = false,
    val isFollowingRefreshing: Boolean = false
) {
    companion object {
        val Fake = FollowListUiState(
            followerList = UiState.Success(
                persistentListOf(
                    FollowItemModel(
                        userId = 1L,
                        profileImgUrl = "https://picsum.photos/200",
                        nickname = "follower_1",
                        followState = FollowState.NONE
                    ),
                    FollowItemModel(
                        userId = 2L,
                        profileImgUrl = "https://picsum.photos/201",
                        nickname = "follower_2",
                        followState = FollowState.ONLY_FOLLOWED
                    )
                )
            ),
            followingList = UiState.Success(
                persistentListOf(
                    FollowItemModel(
                        userId = 3L,
                        profileImgUrl = "https://picsum.photos/202",
                        nickname = "following_1",
                        followState = FollowState.ONLY_FOLLOWING
                    ),
                    FollowItemModel(
                        userId = 4L,
                        profileImgUrl = "https://picsum.photos/203",
                        nickname = "following_2",
                        followState = FollowState.MUTUAL_FOLLOW
                    )
                )
            )
        )
    }
}
