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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.util.UiState
import com.hilingual.presentation.feed.model.FeedListItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class FeedViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<FeedUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<FeedUiState>> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<FeedSideEffect>()
    val sideEffect: SharedFlow<FeedSideEffect> = _sideEffect.asSharedFlow()

    init {
        _uiState.value = UiState.Success(
            FeedUiState(
                myProfileUrl = "https://avatars.githubusercontent.com/u/101113025?v=4",
                recommendFeedList = kotlinx.collections.immutable.persistentListOf(
                    FeedListItemUiModel(
                        userId = 101L,
                        profileUrl = "https://avatars.githubusercontent.com/u/101113025?v=4",
                        nickname = "가짜유저",
                        streak = 7,
                        sharedDateInMinutes = 20,
                        content = "가짜 피드에서 온 데이터!",
                        imageUrl = null,
                        diaryId = 1L,
                        likeCount = 5,
                        isLiked = false
                    )
                ),
                followingFeedList = kotlinx.collections.immutable.persistentListOf(),
                hasFollowing = false
            )
        )
    }

    fun readAllFeed() {
        viewModelScope.launch {
            _sideEffect.emit(FeedSideEffect.ShowToast("피드의 일기를 모두 확인했어요."))
        }
    }
}

sealed interface FeedSideEffect {
    data class ShowToast(val message: String) : FeedSideEffect
}
