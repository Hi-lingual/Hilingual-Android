package com.hilingual.presentation.feedprofile.follow

import androidx.compose.runtime.Immutable
import com.hilingual.core.common.util.UiState
import com.hilingual.data.feed.model.FollowState
import com.hilingual.presentation.feedprofile.follow.model.FollowItemModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class FollowListUiState(
    val followerList: UiState<ImmutableList<FollowItemModel>> = UiState.Loading,
    val followingList: UiState<ImmutableList<FollowItemModel>> = UiState.Loading
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
