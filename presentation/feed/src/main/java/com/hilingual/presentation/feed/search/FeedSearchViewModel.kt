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
package com.hilingual.presentation.feed.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
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
        val searchWord = _uiState.value.searchWord
        if (searchWord.isBlank()) {
            _uiState.update { it.copy(searchResultUserList = UiState.Success(persistentListOf())) }
            return
        }

        viewModelScope.launch {
            feedRepository.getUserSearchResult(searchWord)
                .onSuccess { searchResult ->
                    _uiState.update {
                        it.copy(searchResultUserList = UiState.Success(searchResult.toState()))
                    }
                }.onLogFailure { }
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
                val currentListState = _uiState.value.searchResultUserList
                if (currentListState !is UiState.Success) return@onSuccess

                val updatedList = currentListState.data.map { user ->
                    if (user.userId == userId) {
                        user.copy(
                            followState = FollowState.getValueByFollowState(
                                isFollowing = !currentIsFollowing,
                                isFollowed = user.followState.isFollowed
                            )
                        )
                    } else {
                        user
                    }
                }.toImmutableList()

                _uiState.update {
                    it.copy(searchResultUserList = UiState.Success(updatedList))
                }
            }.onLogFailure { }
        }
    }
}
