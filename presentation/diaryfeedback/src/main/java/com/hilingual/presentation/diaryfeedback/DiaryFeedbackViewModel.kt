package com.hilingual.presentation.diaryfeedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryFeedbackViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<DiaryFeedbackUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<DiaryFeedbackUiState>> = _uiState.asStateFlow()
    
    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            delay(500)

            val diaryContent = dummyDiaryContent
            val feedbacks = dummyFeedbacks
            val recommendExpression = dummyRecommendExpressions

            _uiState.value = UiState.Success(
                DiaryFeedbackUiState(
                    diaryId = 0L, //TODO: 홈이나 일기 작성에서 id 받아오기
                    diaryContent = diaryContent,
                    feedbackList = feedbacks,
                    recommendExpression = recommendExpression,
                    isLoading = false
                )
            )
        }
    }

    private fun toggleDiaryShowOption(isAI: Boolean) {
        _uiState.update {
            (it as UiState.Success).copy(
                data = it.data.copy(isAI = isAI)
            )
        }
    }

    companion object {
        //TODO: 서버 붙인 이후 삭제
        val dummyDiaryContent = DiaryContent(
            writtenDate = "8월 21일 목요일",
            originalText = "",
            aiText = "",
            diffRanges = persistentListOf(
                Pair(84, 164),
                Pair(278, 316),
                Pair(508, 583),
                Pair(740, 802),
            ),
            imageUrl = ""
        )

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
            ),
        )
        
        val dummyRecommendExpressions = persistentListOf(
            RecommendExpression(
                phraseId = 0L,
                phraseType = persistentListOf("동사", "숙어"),
                phrase = "come across as",
                explanation = "~처럼 보이다, ~한 인상을 주다",
                reason = "“My life comes across as a disaster.”처럼 자신이나 상황의 ‘이미지’를 묘사할 때 자연스러워요.",
                isMarked = true,
            )
        )
    }
}