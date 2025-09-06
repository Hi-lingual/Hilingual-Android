package com.hilingual.presentation.feedprofile.follow

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.util.UiState
import com.hilingual.data.user.model.FollowState
import com.hilingual.data.user.repository.UserRepository
import com.hilingual.presentation.feedprofile.follow.model.FollowItemModel
import com.hilingual.presentation.feedprofile.follow.model.toFollowItemModel
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

    private val targetUserId: Long = savedStateHandle.get<Long>("userId") ?: 0L

    private val _uiState = MutableStateFlow(FollowListUiState())
    val uiState: StateFlow<FollowListUiState> = _uiState.asStateFlow()

    init {
        loadFollowers()
        loadFollowings()
    }

    private fun loadFollowers() {
        viewModelScope.launch {
            userRepository.getFollowers(targetUserId)
                .onSuccess { followUserList ->
                    val followerItems = followUserList.map { it.toFollowItemModel() }.toImmutableList()
                    _uiState.update {
                        it.copy(followerList = UiState.Success(followerItems))
                    }
                }
                .onLogFailure {
                }
        }
    }

    private fun loadFollowings() {
        viewModelScope.launch {
            userRepository.getFollowings(targetUserId)
                .onSuccess { followUserList ->
                    val followingItems = followUserList.map { it.toFollowItemModel() }.toImmutableList()
                    _uiState.update {
                        it.copy(followingList = UiState.Success(followingItems))
                    }
                }
                .onLogFailure {
                }
        }
    }

    internal fun updateFollowingState(userId: Long, isFollowing: Boolean) {
        viewModelScope.launch {
            val result = if (isFollowing) {
                userRepository.deleteFollow(userId)
            } else {
                userRepository.putFollow(userId)
            }

            result.onSuccess {
                _uiState.update { currentState ->
                    val updatedFollowingList = updateFollowingListState(currentState.followingList, userId, isFollowing)
                    val updatedFollowerList = updateFollowerListState(currentState.followerList, userId, isFollowing)

                    currentState.copy(
                        followingList = updatedFollowingList,
                        followerList = updatedFollowerList
                    )
                }
            }.onLogFailure {

            }
        }
    }

    private fun updateFollowingListState(
        currentList: UiState<ImmutableList<FollowItemModel>>,
        userId: Long,
        currentIsFollowing: Boolean
    ): UiState<ImmutableList<FollowItemModel>> {
        return if (currentList is UiState.Success) {
            val updatedList = currentList.data.map { user ->
                if (user.userId == userId) {
                    val newState = FollowState.getValueByFollowState(
                        isFollowing = !currentIsFollowing,
                        isFollowed = user.followState.isFollowed
                    ) ?: user.followState
                    user.copy(followState = newState)
                } else {
                    user
                }
            }.toImmutableList()

            UiState.Success(updatedList)
        } else {
            currentList
        }
    }

    private fun updateFollowerListState(
        currentList: UiState<ImmutableList<FollowItemModel>>,
        userId: Long,
        currentIsFollowing: Boolean
    ): UiState<ImmutableList<FollowItemModel>> {
        return if (currentList is UiState.Success) {
            val updatedList = currentList.data.map { user ->
                if (user.userId == userId) {
                    val newState = FollowState.getValueByFollowState(
                        isFollowing = !currentIsFollowing,
                        isFollowed = user.followState.isFollowed
                    ) ?: user.followState
                    user.copy(followState = newState)
                } else {
                    user
                }
            }.toImmutableList()

            UiState.Success(updatedList)
        } else {
            currentList
        }
    }
}
