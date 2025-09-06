package com.hilingual.presentation.feedprofile.follow

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.util.UiState
import com.hilingual.data.user.model.FollowState
import com.hilingual.data.user.repository.UserRepository
import com.hilingual.presentation.feedprofile.follow.model.FollowItemModel
import com.hilingual.presentation.feedprofile.follow.model.FollowTabType
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
                        FollowTabType.FOLLOWER -> {
                            currentState.copy(
                                followerList = updateUiState(currentState.followerList, userId, isFollowing)
                            )
                        }
                        FollowTabType.FOLLOWING -> {
                            currentState.copy(
                                followingList = updateUiState(currentState.followingList, userId, isFollowing),
                            )
                        }
                    }
                }
            }
                .onLogFailure {

                }
        }
    }

    fun refreshTab(tabType: FollowTabType) {
        when (tabType) {
            FollowTabType.FOLLOWER -> loadFollowers()
            FollowTabType.FOLLOWING -> loadFollowings()
        }
    }

    private fun updateUiState(
        uiState: UiState<ImmutableList<FollowItemModel>>,
        userId: Long,
        currentIsFollowing: Boolean
    ): UiState<ImmutableList<FollowItemModel>> {
        return if (uiState is UiState.Success) {
            val updatedList = updateFollowItemList(uiState.data, userId, currentIsFollowing)
            UiState.Success(updatedList)
        } else {
            uiState
        }
    }

    private fun updateFollowItemList(
        followList: ImmutableList<FollowItemModel>,
        userId: Long,
        currentIsFollowing: Boolean
    ): ImmutableList<FollowItemModel> {
        val targetIndex = followList.indexOfFirst { it.userId == userId }
        if (targetIndex == -1) return followList

        val targetItem = followList[targetIndex]
        val newState = FollowState.getValueByFollowState(
            isFollowing = !currentIsFollowing,
            isFollowed = targetItem.followState.isFollowed
        ) ?: targetItem.followState

        val updatedItem = targetItem.copy(followState = newState)

        return followList.toMutableList().apply {
            set(targetIndex, updatedItem)
        }.toImmutableList()
    }
}
