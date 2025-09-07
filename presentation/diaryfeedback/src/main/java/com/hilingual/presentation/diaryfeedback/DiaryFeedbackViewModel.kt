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
package com.hilingual.presentation.diaryfeedback

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.extension.updateSuccess
import com.hilingual.core.common.util.UiState
import com.hilingual.data.diary.model.BookmarkResult
import com.hilingual.data.diary.model.PhraseBookmarkModel
import com.hilingual.data.diary.repository.DiaryRepository
import com.hilingual.presentation.diaryfeedback.navigation.DiaryFeedback
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
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class DiaryFeedbackViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val diaryRepository: DiaryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<DiaryFeedbackUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<DiaryFeedbackUiState>> = _uiState.asStateFlow()

    val diaryId = savedStateHandle.toRoute<DiaryFeedback>().diaryId

    private val _sideEffect = MutableSharedFlow<DiaryFeedbackSideEffect>()
    val sideEffect: SharedFlow<DiaryFeedbackSideEffect> = _sideEffect.asSharedFlow()

    init {
        loadInitialData()
    }

    private suspend fun requestDiaryFeedbackData() {
        val (contentResult, feedbacksResult, recommendExpressionsResult) = coroutineScope {
            val contentDeferred = async { diaryRepository.getDiaryContent(diaryId) }
            val feedbacksDeferred = async { diaryRepository.getDiaryFeedbacks(diaryId) }
            val recommendExpressionsDeferred = async { diaryRepository.getDiaryRecommendExpressions(diaryId) }

            Triple(contentDeferred.await(), feedbacksDeferred.await(), recommendExpressionsDeferred.await())
        }

        if (
            contentResult.isSuccess &&
            feedbacksResult.isSuccess &&
            recommendExpressionsResult.isSuccess
        ) {
            val diaryResult = contentResult.getOrThrow()
            val feedbacks = feedbacksResult.getOrThrow()
            val recommendExpressions = recommendExpressionsResult.getOrThrow()

            val newUiState = DiaryFeedbackUiState(
                isPublished = diaryResult.isPublished,
                writtenDate = diaryResult.writtenDate,
                diaryContent = diaryResult.toState(),
                feedbackList = feedbacks.map { it.toState() }.toImmutableList(),
                recommendExpressionList = recommendExpressions.map { it.toState() }.toImmutableList()
            )
            _uiState.value = UiState.Success(newUiState)
        } else {
            throw Exception()
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            runCatching {
                requestDiaryFeedbackData()
            }.onFailure { e ->
                Timber.d("일기 상세 조회 실패: $e")
                _uiState.value = UiState.Failure
                _sideEffect.emit(
                    DiaryFeedbackSideEffect.ShowRetryDialog { loadInitialData() }
                )
            }
        }
    }

    fun toggleIsPublished(isPublished: Boolean) {
        val currentState = _uiState.value
        if (currentState !is UiState.Success) return

        viewModelScope.launch {
            val result = if (isPublished) {
                diaryRepository.patchDiaryPublish(diaryId)
            } else {
                diaryRepository.patchDiaryUnpublish(diaryId)
            }

            result.onSuccess {
                _uiState.updateSuccess { currentState ->
                    currentState.copy(isPublished = isPublished)
                }
                if (isPublished) {
                    showPublishSnackbar()
                } else {
                    showToast("일기가 비공개되었어요!")
                }
            }.onLogFailure { exception ->
                _uiState.value = UiState.Failure
                _sideEffect.emit(
                    DiaryFeedbackSideEffect.ShowRetryDialog { loadInitialData() }
                )
            }
        }
    }

    fun deleteDiary() {
        viewModelScope.launch {
            diaryRepository.deleteDiary(diaryId = diaryId).onSuccess {
                showToast("삭제가 완료되었어요.")
                _sideEffect.emit(DiaryFeedbackSideEffect.NavigateToHome)
            }
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
                        else -> { }
                    }
                }
                .onLogFailure { }
        }
    }

    private suspend fun showPublishSnackbar() {
        _sideEffect.emit(DiaryFeedbackSideEffect.ShowDiaryPublishSnackbar(message = "일기가 게시되었어요!", actionLabel = "보러가기"))
    }

    private suspend fun showVocaOverflowSnackbar() {
        _sideEffect.emit(DiaryFeedbackSideEffect.ShowVocaOverflowSnackbar(message = "단어장이 모두 찼어요!", actionLabel = "비우러가기"))
    }

    private suspend fun showToast(message: String) {
        _sideEffect.emit(DiaryFeedbackSideEffect.ShowToast(message = message))
    }
}

sealed interface DiaryFeedbackSideEffect {
    data object NavigateToHome : DiaryFeedbackSideEffect
    data class ShowRetryDialog(val onRetry: () -> Unit) : DiaryFeedbackSideEffect
    data class ShowDiaryPublishSnackbar(val message: String, val actionLabel: String) : DiaryFeedbackSideEffect
    data class ShowVocaOverflowSnackbar(val message: String, val actionLabel: String) : DiaryFeedbackSideEffect
    data class ShowToast(val message: String) : DiaryFeedbackSideEffect
}
