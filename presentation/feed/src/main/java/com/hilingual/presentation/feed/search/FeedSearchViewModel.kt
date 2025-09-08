package com.hilingual.presentation.feed.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.util.UiState
import com.hilingual.data.feed.model.FollowState
import com.hilingual.data.feed.repository.FeedRepository
import com.hilingual.data.user.repository.UserRepository
import com.hilingual.presentation.feed.model.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class FeedSearchViewModel @Inject constructor(
    val feedRepository: FeedRepository,
    val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FeedSearchUiState())
    val uiState: StateFlow<FeedSearchUiState> = _uiState.asStateFlow()

    fun updateSearchWord(searchWord: String) {
        _uiState.update { it.copy(searchWord = searchWord) }
    }

    fun searchUser() {
        if (_uiState.value.searchWord.isBlank()) {
            _uiState.update { it.copy(searchResultUserList = UiState.Success(persistentListOf())) }
            return
        }

        viewModelScope.launch {
            feedRepository.getUserSearchResult(_uiState.value.searchWord).onSuccess { searchResult ->
                _uiState.update {
                    it.copy(searchResultUserList = UiState.Success(searchResult.toState()))
                }
            }
        }
    }

    fun clearSearchWord() {
        _uiState.update {
            it.copy(searchWord = "")
        }
    }

    fun updateFollowingState(userId: Long, currentIsFollowing: Boolean) {
        viewModelScope.launch {
            val result = if (currentIsFollowing) {
                userRepository.deleteFollow(userId)
            } else {
                userRepository.putFollow(userId)
            }

            result.onSuccess {
                _uiState.update { currentState ->
                    val oldState = currentState.searchResultUserList

                    if (oldState is UiState.Success) {
                        val updatedList = oldState.data.map { user ->
                            if (user.userId == userId) {
                                val newState = FollowState.getValueByFollowState(
                                    isFollowing = !currentIsFollowing,
                                    isFollowed = user.followState.isFollowed
                                )
                                user.copy(followState = newState)
                            } else {
                                user
                            }
                        }.toImmutableList()

                        currentState.copy(
                            searchResultUserList = UiState.Success(updatedList)
                        )
                    } else {
                        currentState
                    }
                }
            }
        }
    }
}
