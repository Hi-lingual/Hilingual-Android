package com.hilingual.presentation.feedprofile.follow

import androidx.lifecycle.ViewModel
import com.hilingual.core.common.util.UiState
import com.hilingual.presentation.feedprofile.follow.model.FollowItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class FollowListViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(FollowListUiState())
    val uiState: StateFlow<FollowListUiState> = _uiState.asStateFlow()

    init {
        val dummyFollowers = persistentListOf(
            FollowItemModel(
                userId = 1L,
                profileImgUrl = "https://picsum.photos/200",
                nickname = "follower_1",
                isFollowing = false,
                isFollowed = true
            ),
            FollowItemModel(
                userId = 2L,
                profileImgUrl = "https://picsum.photos/201",
                nickname = "follower_2",
                isFollowing = true,
                isFollowed = true
            )
        )

        val dummyFollowings = persistentListOf(
            FollowItemModel(
                userId = 3L,
                profileImgUrl = "https://picsum.photos/202",
                nickname = "following_1",
                isFollowing = true,
                isFollowed = false
            ),
            FollowItemModel(
                userId = 4L,
                profileImgUrl = "https://picsum.photos/203",
                nickname = "following_2",
                isFollowing = true,
                isFollowed = true
            )
        )

        _uiState.value = FollowListUiState(
            followerList = UiState.Success(dummyFollowers),
            followingList = UiState.Success(dummyFollowings)
        )
    }
}
