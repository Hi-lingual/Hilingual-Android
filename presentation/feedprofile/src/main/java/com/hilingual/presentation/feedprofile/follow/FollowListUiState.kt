package com.hilingual.presentation.feedprofile.follow

import androidx.compose.runtime.Immutable
import com.hilingual.core.common.util.UiState
import com.hilingual.presentation.feedprofile.follow.model.FollowItemModel
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class FollowListUiState(
    val followerList: UiState<ImmutableList<FollowItemModel>> = UiState.Loading,
    val followingList: UiState<ImmutableList<FollowItemModel>> = UiState.Loading
)