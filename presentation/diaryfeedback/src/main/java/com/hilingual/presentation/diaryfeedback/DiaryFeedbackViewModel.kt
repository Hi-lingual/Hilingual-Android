package com.hilingual.presentation.diaryfeedback

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.util.UiState
import com.hilingual.data.diary.model.PhraseBookmarkModel
import com.hilingual.data.diary.repository.DiaryRepository
import com.hilingual.presentation.diaryfeedback.model.toState
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
import kotlinx.coroutines.flow.update
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

    private suspend fun requestDiaryFeedbackData() = coroutineScope {
        val contentDeferred = async { diaryRepository.getDiaryContent(diaryId) }
        val feedbacksDeferred = async { diaryRepository.getDiaryFeedbacks(diaryId) }
        val recommendExpressionsDeferred = async { diaryRepository.getDiaryRecommendExpressions(diaryId) }

        val contentResult = contentDeferred.await()
        val feedbacksResult = feedbacksDeferred.await()
        val recommendExpressionsResult = recommendExpressionsDeferred.await()

        if (
            contentResult.isSuccess &&
            feedbacksResult.isSuccess &&
            recommendExpressionsResult.isSuccess
        ) {
            val diaryResult = contentResult.getOrThrow()
            val feedbacks = feedbacksResult.getOrThrow()
            val recommendExpressions = recommendExpressionsResult.getOrThrow()

            val newUiState = DiaryFeedbackUiState(
                writtenDate = diaryResult.writtenDate,
                diaryContent = diaryResult.toState(),
                feedbackList = feedbacks.map { it.toState() }.toImmutableList(),
                recommendExpressionList = recommendExpressions.map { it.toState() }.toImmutableList()
            )
            _uiState.value = UiState.Success(newUiState)
        } else {
            throw Exception("API error")
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

    fun toggleBookmark(phraseId: Long, isMarked: Boolean) {
        viewModelScope.launch {
            diaryRepository.patchPhraseBookmark(
                phraseId = phraseId,
                bookmarkModel = PhraseBookmarkModel(isMarked)
            )
                .onSuccess {
                    _uiState.update { currentState ->

                        val successState = currentState as UiState.Success
                        val oldList = successState.data.recommendExpressionList

                        val updatedList = oldList.map { item ->
                            if (item.phraseId == phraseId) {
                                item.copy(isMarked = isMarked)
                            } else {
                                item
                            }
                        }.toImmutableList()

                        successState.copy(
                            data = successState.data.copy(recommendExpressionList = updatedList)
                        )
                    }
                }
                .onLogFailure { }
        }
    }
}

sealed interface DiaryFeedbackSideEffect {
    data class ShowRetryDialog(val onRetry: () -> Unit) : DiaryFeedbackSideEffect
}
