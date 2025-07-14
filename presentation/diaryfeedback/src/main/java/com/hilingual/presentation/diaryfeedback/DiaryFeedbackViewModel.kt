package com.hilingual.presentation.diaryfeedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.util.UiState
import com.hilingual.data.diary.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
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
    private val _uiState = MutableStateFlow<UiState<DiaryFeedbackUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<DiaryFeedbackUiState>> = _uiState.asStateFlow()

    init {
        getDiaryContent()
    }

    private fun getDiaryContent() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            diaryRepository.getDiaryContent(9L)
                .onSuccess { diaryResult ->
                    _uiState.value = UiState.Success(
                        DiaryFeedbackUiState(
                            writtenDate = diaryResult.writtenDate,
                            diaryContent = DiaryContent(
                                originalText = diaryResult.originalText,
                                aiText = diaryResult.rewriteText,
                                diffRanges = diaryResult.diffRanges.map {
                                    Pair(it.diffRange.first, it.diffRange.second)
                                }.toImmutableList(),
                                imageUrl = diaryResult.imageUrl
                            ),
                            isLoading = false
                        )
                    )
                }
                .onLogFailure {
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

    companion object {
        val dummyFeedbacks = persistentListOf(
            FeedbackContent(
                originalText = "i’m drinking milk because I easily get stomachache",
                feedbackText = "I’m drinking milk because I get stomachaches easily",
                explain = "a stomachache처럼 가산명사는 ~게 작성하는게 맞는 표현이에요. ‘easily’의 어순을 문장 마지막에 작성하여 더 정확해졌어요."
            ),
            FeedbackContent(
                originalText = "I was planning to arrive it here around 13:30",
                feedbackText = "I was planning to arrive here around 1:30 p.m",
                explain = "arrive는 자동사이기 때문에 직접 목적어 ‘it’을 쓸 수 없어요. ‘arrive at the station’, ‘arrive here’처럼 써야 맞는 표현이에요!"
            )
        )
        val dummyEmptyFeedback = persistentListOf<FeedbackContent>()

        val dummyRecommendExpressions = persistentListOf(
            RecommendExpression(
                phraseId = 0L,
                phraseType = persistentListOf("동사", "숙어"),
                phrase = "come across as",
                explanation = "~처럼 보이다, ~한 인상을 주다",
                reason = "“My life comes across as a disaster.”처럼 자신이나 상황의 ‘이미지’를 묘사할 때 자연스러워요.",
                isMarked = false
            ),
            RecommendExpression(
                phraseId = 1L,
                phraseType = persistentListOf("형용사"),
                phrase = "underwhelming",
                explanation = "기대에 못 미치는, 실망스러운",
                reason = "“The weather was kind of underwhelming.”처럼 예상보다 실망스러운 상황을 부드럽게 말할 수 있어요."
            ),
            RecommendExpression(
                phraseId = 2L,
                phraseType = persistentListOf("명사"),
                phrase = "underwhelming",
                explanation = "기대에 못 미치는, 실망스러운",
                reason = "“The weather was kind of underwhelming.”처럼 예상보다 실망스러운 상황을 부드럽게 말할 수 있어요."
            )
        )
    }
}
