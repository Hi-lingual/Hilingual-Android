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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
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

    fun toggleIsLiked(diaryId: Long, isLiked: Boolean) {
        viewModelScope.launch {
            // TODO: API 성공 후 좋아요 수 증가
            _uiState.update { currentState ->
                val successState = currentState as UiState.Success

                val updatedRecommendList = updateSingleItem(
                    list = successState.data.recommendFeedList.toPersistentList(),
                    diaryId = diaryId
                ) { item ->
                    item.copy(
                        isLiked = isLiked,
                        likeCount = item.likeCount + if (isLiked) 1 else -1
                    )
                }

                val updatedFollowingList = updateSingleItem(
                    list = successState.data.followingFeedList.toPersistentList(),
                    diaryId = diaryId
                ) { item ->
                    item.copy(
                        isLiked = isLiked,
                        likeCount = item.likeCount + if (isLiked) 1 else -1
                    )
                }

                successState.copy(
                    data = successState.data.copy(
                        recommendFeedList = updatedRecommendList,
                        followingFeedList = updatedFollowingList
                    )
                )
            }
        }
    }

    fun diaryUnpublish(diaryId: Long) {
        // TODO: API 호출 성공 후 표시
        viewModelScope.launch {
            _sideEffect.emit(FeedSideEffect.ShowToast(message = "일기가 비공개 되었어요."))
            _uiState.update { currentState ->
                val successState = currentState as UiState.Success
                successState.copy(
                    data = successState.data.copy(
                        recommendFeedList = removeSingleItem(
                            list = successState.data.recommendFeedList.toPersistentList(),
                            diaryId = diaryId
                        ),
                        followingFeedList = removeSingleItem(
                            list = successState.data.followingFeedList.toPersistentList(),
                            diaryId = diaryId
                        )
                    )
                )
            }
        }
    }

    private fun updateSingleItem(
        list: PersistentList<FeedListItemUiModel>,
        diaryId: Long,
        transform: (FeedListItemUiModel) -> FeedListItemUiModel
    ): ImmutableList<FeedListItemUiModel> {
        val index = list.indexOfFirst { it.diaryId == diaryId }
        return if (index != -1) {
            val updatedItem = transform(list[index])
            list.set(index, updatedItem)
        } else {
            list
        }
    }

    private fun removeSingleItem(
        list: PersistentList<FeedListItemUiModel>,
        diaryId: Long
    ): ImmutableList<FeedListItemUiModel> {
        val index = list.indexOfFirst { it.diaryId == diaryId }
        return if (index != -1) {
            list.removeAt(index)
        } else {
            list
        }
    }
}

sealed interface FeedSideEffect {
    data class ShowToast(val message: String) : FeedSideEffect
}
