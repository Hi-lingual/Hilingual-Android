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
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.util.UiState
import com.hilingual.data.diary.repository.DiaryRepository
import com.hilingual.data.feed.repository.FeedRepository
import com.hilingual.presentation.feed.model.FeedItemUiModel
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
internal class FeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val diaryRepository: DiaryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<FeedSideEffect>()
    val sideEffect: SharedFlow<FeedSideEffect> = _sideEffect.asSharedFlow()

    fun loadFeedData() {
        getRecommendFeeds()
        getFollowingFeeds()
    }

    fun readAllFeed() {
        viewModelScope.launch {
            _sideEffect.emit(FeedSideEffect.ShowToast("피드의 일기를 모두 확인했어요."))
        }
    }

    private fun getRecommendFeeds() {
        viewModelScope.launch {
            feedRepository.getRecommendFeeds()
                .onSuccess { feedResult ->
                    _uiState.update {
                        it.copy(
                            recommendFeedList = UiState.Success(
                                feedResult.toState().toImmutableList()
                            )
                        )
                    }
                }
                .onLogFailure { }
        }
    }

    private fun getFollowingFeeds() {
        viewModelScope.launch {
            feedRepository.getFollowingFeeds()
                .onSuccess { feedResult ->
                    _uiState.update {
                        it.copy(
                            followingFeedList = UiState.Success(
                                feedResult.toState().toImmutableList()
                            ),
                            hasFollowing = feedResult.hasFollowing
                        )
                    }
                }
                .onLogFailure { }
        }
    }

    fun toggleIsLiked(diaryId: Long, isLiked: Boolean) {
        viewModelScope.launch {
            feedRepository.postIsLike(diaryId, isLiked).onSuccess {
                _uiState.update { currentState ->
                    currentState.copy(
                        recommendFeedList = currentState.recommendFeedList.updateIfSuccess { list ->
                            updateSingleItem(
                                list = list.toPersistentList(),
                                diaryId = diaryId
                            ) { item ->
                                item.copy(
                                    isLiked = isLiked,
                                    likeCount = item.likeCount + if (isLiked) 1 else -1
                                )
                            }
                        },
                        followingFeedList = currentState.followingFeedList.updateIfSuccess { list ->
                            updateSingleItem(
                                list = list.toPersistentList(),
                                diaryId = diaryId
                            ) { item ->
                                item.copy(
                                    isLiked = isLiked,
                                    likeCount = (item.likeCount + if (isLiked) 1 else -1).coerceAtLeast(0)
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    fun diaryUnpublish(diaryId: Long) {
        viewModelScope.launch {
            diaryRepository.patchDiaryUnpublish(diaryId)
                .onSuccess {
                    _sideEffect.emit(FeedSideEffect.ShowToast(message = "일기가 비공개 되었어요."))
                    _uiState.update { currentState ->
                        currentState.copy(
                            recommendFeedList = currentState.recommendFeedList.updateIfSuccess { list ->
                                removeSingleItem(list.toPersistentList(), diaryId)
                            },
                            followingFeedList = currentState.followingFeedList.updateIfSuccess { list ->
                                removeSingleItem(list.toPersistentList(), diaryId)
                            }
                        )
                    }
                }.onLogFailure { }
        }
    }

    private fun UiState<ImmutableList<FeedItemUiModel>>.updateIfSuccess(
        transform: (ImmutableList<FeedItemUiModel>) -> ImmutableList<FeedItemUiModel>
    ): UiState<ImmutableList<FeedItemUiModel>> {
        return when (this) {
            is UiState.Success -> UiState.Success(transform(this.data))
            else -> this
        }
    }

    private fun updateSingleItem(
        list: PersistentList<FeedItemUiModel>,
        diaryId: Long,
        transform: (FeedItemUiModel) -> FeedItemUiModel
    ): ImmutableList<FeedItemUiModel> {
        val index = list.indexOfFirst { it.diaryId == diaryId }
        return if (index != -1) {
            val updatedItem = transform(list[index])
            list.set(index, updatedItem)
        } else {
            list
        }
    }

    private fun removeSingleItem(
        list: PersistentList<FeedItemUiModel>,
        diaryId: Long
    ): ImmutableList<FeedItemUiModel> {
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
