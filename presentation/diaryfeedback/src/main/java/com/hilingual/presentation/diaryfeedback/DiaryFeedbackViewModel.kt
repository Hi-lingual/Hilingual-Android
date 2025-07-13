package com.hilingual.presentation.diaryfeedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DiaryFeedbackViewModel @Inject constructor() : ViewModel() {
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
                    recommendExpressionList = recommendExpression,
                    isLoading = false
                )
            )
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
        //TODO: 서버 붙인 이후 삭제
        val dummyDiaryContent = DiaryContent(
            originalText = "Today I went to the cafe Conhas in Yeonnam to meet my teammates.\u2028 I was planning to arrive around 1:30 p.m., but I got there at 2:20 because I overslept, as always.\u2028 I wore rain boots and brought my favorite umbrella because the weather forecast said it would rain all day, but it wasn’t really raining much outside.\u2028 I got kind of disappointed. But yes, no rain is better than rain, I guess.\n" +
                    "After arriving, I had a jambon arugula sandwich with a vanilla latte.\u2028 Honestly, I should be more careful when I'm drinking milk because I get stomachaches easily, but I always order lattes.\u2028My life feels like a disaster, a mess that I call myself.\u2028 But they tasted really good, so I felt more motivated to work.\u2028 I really liked this café because it's spacious, chill, and has a great atmosphere for focusing.\u2028 I’ll definitely come back again soon!",
            aiText = "Today I went to the cafe Conhas in Yeonnam to meet my teammates.\n I was planning to arrive around 1:30 p.m., but I got there at 2:20 because I overslept, as always.\n I wore rain boots and brought my favorite umbrella because the weather forecast said it would rain all day, but it wasn’t really raining much outside.\n I got kind of disappointed. But yes, no rain is better than rain, I guess.\n" +
                    "After arriving, I had a jambon arugula sandwich with a vanilla latte.\n Honestly, I should be more careful when I'm drinking milk because I get stomachaches easily, but I always order lattes.\nMy life feels like a disaster, a mess that I call myself.\n But they tasted really good, so I felt more motivated to work.\n I really liked this café because it's spacious, chill, and has a great atmosphere for focusing.\n I’ll definitely come back again soon!",
            diffRanges = persistentListOf(
                Pair(84, 164),
                Pair(278, 316),
                Pair(508, 583),
                Pair(740, 802),
            ),
            imageUrl = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/29/cb/cc/85/caption.jpg?w=600&h=400&s=1"
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
        val dummyEmptyFeedback = persistentListOf<FeedbackContent>()
        
        val dummyRecommendExpressions = persistentListOf(
            RecommendExpression(
                phraseId = 0L,
                phraseType = persistentListOf("동사", "숙어"),
                phrase = "come across as",
                explanation = "~처럼 보이다, ~한 인상을 주다",
                reason = "“My life comes across as a disaster.”처럼 자신이나 상황의 ‘이미지’를 묘사할 때 자연스러워요.",
                isMarked = false,
            ),
            RecommendExpression(
                phraseId = 1L,
                phraseType = persistentListOf("형용사"),
                phrase = "underwhelming",
                explanation = "기대에 못 미치는, 실망스러운",
                reason = "“The weather was kind of underwhelming.”처럼 예상보다 실망스러운 상황을 부드럽게 말할 수 있어요.",
            ),
            RecommendExpression(
                phraseId = 2L,
                phraseType = persistentListOf("명사"),
                phrase = "underwhelming",
                explanation = "기대에 못 미치는, 실망스러운",
                reason = "“The weather was kind of underwhelming.”처럼 예상보다 실망스러운 상황을 부드럽게 말할 수 있어요.",
            ),
        )
    }
}