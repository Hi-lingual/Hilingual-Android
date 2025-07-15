package com.hilingual.presentation.diaryfeedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.extension.updateSuccess
import com.hilingual.core.common.util.UiState
import com.hilingual.data.diary.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
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
        getDiaryContent()
        getFeedbacks()
        getRecommendExpressions()
    }

    private fun getDiaryContent() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            diaryRepository.getDiaryContent(diaryId)
                .onSuccess { diaryResult ->
                    _uiState.value = UiState.Success(
                        DiaryFeedbackUiState(
                            writtenDate = diaryResult.writtenDate,
                            diaryContent = DiaryContent(
                                originalText = diaryResult.originalText,
                                aiText = diaryResult.rewriteText,
                                diffRanges = diaryResult.diffRanges.map {
                                    it.diffRange.first to it.diffRange.second
                                }.toImmutableList(),
                                imageUrl = diaryResult.imageUrl
                            )
                        )
                    )
                }

                .onLogFailure { }
        }
    }

    private fun getFeedbacks() {
        viewModelScope.launch {
            diaryRepository.getDiaryFeedbacks(diaryId)
                .onSuccess { feedbacks ->
                    val currentState = _uiState.value
                    if (currentState !is UiState.Success) return@launch

                    _uiState.updateSuccess {
                        it.copy(
                            feedbackList = feedbacks.map {
                                FeedbackContent(
                                    originalText = it.originalText,
                                    feedbackText = it.rewriteText,
                                    explain = it.explain,
                                )
                            }.toImmutableList(),
                        )
                    }
                }
                .onLogFailure { }
        }
    }

    private fun getRecommendExpressions() {
        viewModelScope.launch {
            diaryRepository.getDiaryRecommendExpressions(diaryId)
                .onSuccess { expressions ->
                    val currentState = _uiState.value
                    if (currentState !is UiState.Success) return@launch

                    _uiState.updateSuccess {
                        it.copy(
                            recommendExpressionList = expressions.map {
                                RecommendExpression(
                                    phraseId = it.phraseId,
                                    phraseType = it.phraseType.toImmutableList(),
                                    phrase = it.phrase,
                                    explanation = it.explanation,
                                    reason = it.reason,
                                    isMarked = it.isMarked
                                )
                            }.toImmutableList(),
                        )
                    }
                }
                .onLogFailure { }
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
