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
package com.hilingual.presentation.feeddiary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.extension.updateSuccess
import com.hilingual.core.common.util.UiState
import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.diary.model.BookmarkResult
import com.hilingual.data.diary.model.PhraseBookmarkModel
import com.hilingual.data.diary.repository.DiaryRepository
import com.hilingual.data.feed.repository.FeedRepository
import com.hilingual.data.user.repository.UserRepository
import com.hilingual.presentation.feeddiary.navigation.FeedDiary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
internal class FeedDiaryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val diaryRepository: DiaryRepository,
    private val feedRepository: FeedRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val diaryId = savedStateHandle.toRoute<FeedDiary>().diaryId

    private val _uiState = MutableStateFlow<UiState<FeedDiaryUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<FeedDiaryUiState>> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<FeedDiarySideEffect>()
    val sideEffect: SharedFlow<FeedDiarySideEffect> = _sideEffect.asSharedFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val profileResult = feedRepository.getFeedDiaryProfile(diaryId)

            if (profileResult.isFailure) {
                _sideEffect.emit(FeedDiarySideEffect.ShowErrorDialog)
                return@launch
            }
            val profileInfo = profileResult.getOrThrow()

            suspendRunCatching {
                coroutineScope {
                    val contentDeferred = async { diaryRepository.getDiaryContent(diaryId) }
                    val feedbacksDeferred = async { diaryRepository.getDiaryFeedbacks(diaryId) }
                    val recommendExpressionsDeferred =
                        async { diaryRepository.getDiaryRecommendExpressions(diaryId) }

                    val contentResult = contentDeferred.await().getOrThrow()
                    val feedbacksResult = feedbacksDeferred.await().getOrThrow()
                    val recommendExpressionsResult =
                        recommendExpressionsDeferred.await().getOrThrow()

                    FeedDiaryUiState(
                        isMine = profileInfo.isMine,
                        profileContent = profileInfo.toState(),
                        writtenDate = contentResult.writtenDate,
                        diaryContent = contentResult.toState(),
                        feedbackList = feedbacksResult.map { it.toState() }.toImmutableList(),
                        recommendExpressionList = recommendExpressionsResult.map { it.toState() }
                            .toImmutableList()
                    )
                }
            }.onSuccess { combinedState ->
                _uiState.update { UiState.Success(combinedState) }
            }.onLogFailure {
                _sideEffect.emit(FeedDiarySideEffect.ShowErrorDialog)
            }
        }
    }

    fun toggleIsLiked(isLiked: Boolean) {
        viewModelScope.launch {
            feedRepository.postIsLike(diaryId, isLiked)
                .onSuccess {
                    _uiState.updateSuccess {
                        it.copy(
                            profileContent = it.profileContent.copy(
                                isLiked = isLiked,
                                likeCount = (it.profileContent.likeCount + if (isLiked) 1 else -1)
                                    .coerceAtLeast(0)
                            )
                        )
                    }
                }
                .onLogFailure { }
        }
    }

    fun blockUser(userId: Long) {
        viewModelScope.launch {
            userRepository.putBlockUser(userId)
                .onSuccess {
                    _sideEffect.emit(FeedDiarySideEffect.NavigateToFeedProfile(userId))
                }.onLogFailure { }
        }
    }

    fun toggleBookmark(phraseId: Long, isMarked: Boolean) {
        val currentState = _uiState.value
        if (currentState !is UiState.Success) return

        viewModelScope.launch {
            diaryRepository.patchPhraseBookmark(
                phraseId = phraseId,
                bookmarkModel = PhraseBookmarkModel(isMarked)
            )
                .onSuccess { result ->
                    when (result) {
                        BookmarkResult.SUCCESS -> {
                            _uiState.updateSuccess { currentState ->

                                val oldList = currentState.recommendExpressionList

                                val updatedList = oldList.map { item ->
                                    if (item.phraseId == phraseId) {
                                        item.copy(isMarked = isMarked)
                                    } else {
                                        item
                                    }
                                }.toImmutableList()

                                currentState.copy(
                                    recommendExpressionList = updatedList
                                )
                            }
                        }

                        BookmarkResult.OVERCAPACITY -> {
                            showVocaOverflowSnackbar()
                        }

                        else -> {} // 성공, 실패 외 기타 처리
                    }
                }
                .onLogFailure { }
        }
    }

    fun diaryUnpublish() {
        viewModelScope.launch {
            diaryRepository.patchDiaryUnpublish(diaryId)
                .onSuccess {
                    _sideEffect.emit(FeedDiarySideEffect.ShowToast(message = "일기가 비공개 되었어요."))
                    _sideEffect.emit(FeedDiarySideEffect.NavigateToUp)
                }.onLogFailure { }
        }
    }

    private suspend fun showVocaOverflowSnackbar() {
        _sideEffect.emit(
            FeedDiarySideEffect.ShowVocaOverflowSnackbar(
                message = "단어장이 모두 찼어요!",
                actionLabel = "비우러가기"
            )
        )
    }
}

sealed interface FeedDiarySideEffect {
    data object NavigateToUp : FeedDiarySideEffect
    data class NavigateToFeedProfile(val userId: Long) : FeedDiarySideEffect
    data class ShowVocaOverflowSnackbar(val message: String, val actionLabel: String) :
        FeedDiarySideEffect

    data class ShowToast(val message: String) : FeedDiarySideEffect

    data object ShowErrorDialog : FeedDiarySideEffect
}
