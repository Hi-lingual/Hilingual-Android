package com.hilingual.presentation.diaryfeedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.util.UiState
import com.hilingual.data.diary.repository.DiaryRepository
import com.hilingual.presentation.diaryfeedback.model.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DiaryFeedbackViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository
) : ViewModel() {
    val diaryId: Long = 18L // TODO: 수정 필요

    private val _uiState = MutableStateFlow<UiState<DiaryFeedbackUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<DiaryFeedbackUiState>> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
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
                    diaryId = diaryId,
                    writtenDate = diaryResult.writtenDate,
                    diaryContent = diaryResult.toState(),
                    feedbackList = feedbacks.map {
                        it.toState()
                    }.toImmutableList(),
                    recommendExpressionList = recommendExpressions.map {
                        it.toState()
                    }.toImmutableList()
                )
                _uiState.value = UiState.Success(newUiState)
            } else {
                _uiState.value = UiState.Failure
            }
        }
    }

    fun toggleDiaryShowOption(isAI: Boolean) {
        _uiState.update {
            (it as UiState.Success).copy(
                data = it.data.copy(isAIWritten = isAI)
            )
        }
    }

    fun toggleBookmark(phraseId: Long, isMarked: Boolean) {
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
}
