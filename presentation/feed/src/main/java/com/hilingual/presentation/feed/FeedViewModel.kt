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
import com.hilingual.presentation.feed.model.FeedTabType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class FeedViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<FeedUiState>>(UiState.Success(FeedUiState.Fake))
    val uiState: StateFlow<UiState<FeedUiState>> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<FeedSideEffect>()
    val sideEffect: SharedFlow<FeedSideEffect> = _sideEffect.asSharedFlow()

    fun readAllFeed() {
        viewModelScope.launch {
            _sideEffect.emit(FeedSideEffect.ShowToast("피드의 일기를 모두 확인했어요."))
        }
    }

    fun toggleIsLiked(feedTabType: FeedTabType, diaryId: Long, isLiked: Boolean) {
        viewModelScope.launch {
            // TODO: API 성공 후 좋아요 수 증가
            _uiState.update { currentState ->
                val successState = currentState as UiState.Success

                fun updateFeedItem(item: FeedListItemUiModel): FeedListItemUiModel {
                    return if (item.diaryId == diaryId) {
                        item.copy(
                            isLiked = isLiked,
                            likeCount = item.likeCount + if (isLiked) 1 else -1
                        )
                    } else {
                        item
                    }
                }

                when (feedTabType) {
                    FeedTabType.RECOMMEND -> {
                        successState.copy(
                            data = successState.data.copy(
                                recommendFeedList = successState.data.recommendFeedList
                                    .map(::updateFeedItem)
                                    .toImmutableList()
                            )
                        )
                    }
                    FeedTabType.FOLLOWING -> {
                        successState.copy(
                            data = successState.data.copy(
                                followingFeedList = successState.data.followingFeedList
                                    .map(::updateFeedItem)
                                    .toImmutableList()
                            )
                        )
                    }
                }
            }
        }
    }
}

sealed interface FeedSideEffect {
    data class ShowToast(val message: String) : FeedSideEffect
}
