package com.hilingual.presentation.feedprofile.follow

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.util.UiState
import com.hilingual.data.user.model.follow.FollowState
import com.hilingual.data.user.repository.UserRepository
import com.hilingual.presentation.feedprofile.follow.model.FollowItemModel
import com.hilingual.presentation.feedprofile.follow.model.FollowTabType
import com.hilingual.presentation.feedprofile.follow.model.toState
import com.hilingual.presentation.feedprofile.profile.navigation.FollowList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class FollowListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val targetUserId: Long = savedStateHandle.toRoute<FollowList>().userId

    private val _uiState = MutableStateFlow(FollowListUiState())
    val uiState: StateFlow<FollowListUiState> = _uiState.asStateFlow()

    fun refreshTab(tabType: FollowTabType) {
        viewModelScope.launch {
            setRefreshing(tabType, true)
            loadFollowList(tabType)
            setRefreshing(tabType, false)
        }
    }

    fun updateFollowingState(userId: Long, isFollowing: Boolean, tabType: FollowTabType) {
        viewModelScope.launch {
            val result = if (isFollowing) {
                userRepository.deleteFollow(userId)
            } else {
                userRepository.putFollow(userId)
            }

            result.onSuccess {
                _uiState.update { currentState ->
                    when (tabType) {
                        FollowTabType.FOLLOWER -> currentState.copy(
                            followerList = currentState.followerList.updateFollowState(userId, isFollowing)
                        )
                        FollowTabType.FOLLOWING -> currentState.copy(
                            followingList = currentState.followingList.updateFollowState(userId, isFollowing)
                        )
                    }
                }
            }.onLogFailure { }
        }
    }

    private fun loadFollowList(tabType: FollowTabType) {
        viewModelScope.launch {
            updateLoadingState(tabType, UiState.Loading)

            val result = when (tabType) {
                FollowTabType.FOLLOWER -> userRepository.getFollowers(targetUserId)
                FollowTabType.FOLLOWING -> userRepository.getFollowings(targetUserId)
            }

            result.onSuccess { followUserList ->
                val items = followUserList.map { it.toState() }.toImmutableList()
                updateLoadingState(tabType, UiState.Success(items))
            }.onLogFailure { }
        }
    }

    private fun updateLoadingState(
        tabType: FollowTabType,
        state: UiState<ImmutableList<FollowItemModel>>
    ) {
        _uiState.update { currentState ->
            when (tabType) {
                FollowTabType.FOLLOWER -> currentState.copy(followerList = state)
                FollowTabType.FOLLOWING -> currentState.copy(followingList = state)
            }
        }
    }

    private fun setRefreshing(tabType: FollowTabType, isRefreshing: Boolean) {
        _uiState.update { currentState ->
            when (tabType) {
                FollowTabType.FOLLOWER -> currentState.copy(isFollowerRefreshing = isRefreshing)
                FollowTabType.FOLLOWING -> currentState.copy(isFollowingRefreshing = isRefreshing)
            }
        }
    }
}

private fun UiState<ImmutableList<FollowItemModel>>.updateFollowState(
    userId: Long,
    currentIsFollowing: Boolean
): UiState<ImmutableList<FollowItemModel>> {
    return when (this) {
        is UiState.Success -> {
            val updatedList = data.updateFollowItem(userId, currentIsFollowing)
            UiState.Success(updatedList)
        }
        else -> this
    }
}

private fun ImmutableList<FollowItemModel>.updateFollowItem(
    userId: Long,
    currentIsFollowing: Boolean
): ImmutableList<FollowItemModel> {
    return map { item ->
        if (item.userId == userId) {
            val newState = FollowState.getValueByFollowState(
                isFollowing = !currentIsFollowing,
                isFollowed = item.followState.isFollowed
            )
            item.copy(followState = newState)
        } else {
            item
        }
    }.toImmutableList()
}
