package com.hilingual.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<HomeUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<HomeUiState>> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            delay(500)
            val userProfile = UserProfile(nickname = "슌", profileImg = "https://avatars.githubusercontent.com/u/160750136?v=4", totalDiaries = 12, streak = 4)
            val writtenDates = setOf(LocalDate.now().minusDays(2), LocalDate.now().minusDays(4))

            val selectedDate = LocalDate.now()
            val isWritten = writtenDates.contains(selectedDate)
            val diaryPreview: DiaryPreview?
            val todayTopic: TodayTopic?

            if (isWritten) {
                diaryPreview = DiaryPreview(
                    diaryId = 1,
                    createdAt = "2025-06-27T16:30:12",
                    imageUrl = "https://avatars.githubusercontent.com/u/160750136?v=4",
                    originalText = "Today was such a meaningful day. I went to the park..."
                )
                todayTopic = null
            } else {
                diaryPreview = null
                todayTopic = TodayTopic(
                    topicKor = "오늘 가장 기억에 남는 일은?",
                    topicEn = "What was the most memorable moment today?",
                    remainingTime = 1440
                )
            }

            _uiState.value = UiState.Success(
                HomeUiState(
                    userProfile = userProfile,
                    writtenDates = writtenDates,
                    selectedDate = selectedDate,
                    diaryPreview = diaryPreview,
                    todayTopic = todayTopic
                )
            )
        }
    }

    fun onDateSelected(date: LocalDate) {
        val currentData = (_uiState.value as? UiState.Success)?.data ?: return
        if (currentData.selectedDate == date) return

        _uiState.update {
            (it as? UiState.Success)?.copy(data = it.data.copy(selectedDate = date, diaryPreview = null, todayTopic = null))
                ?: it
        }
        loadDataForDate(date)
    }

    private fun loadDataForDate(date: LocalDate) {
        viewModelScope.launch {
            delay(150)

            _uiState.update { currentState ->
                if (currentState !is UiState.Success) return@update currentState
                if (currentState.data.selectedDate != date) return@update currentState

                val isWritten = currentState.data.writtenDates.contains(date)
                val updatedData = if (isWritten) {
                    currentState.data.copy(
                        diaryPreview = DiaryPreview(
                            diaryId = 1,
                            createdAt = "2025-06-27T16:30:12",
                            imageUrl = null,
                            originalText = "Today was such a meaningful day. I went to the park..."
                        ),
                        todayTopic = null
                    )
                } else {
                    currentState.data.copy(
                        diaryPreview = null,
                        todayTopic = TodayTopic(
                            topicKor = "오늘 가장 기억에 남는 일은?",
                            topicEn = "What was the most memorable moment today?",
                            remainingTime = 1440
                        )
                    )
                }
                UiState.Success(updatedData)
            }
        }
    }

    fun onMonthChanged(yearMonth: YearMonth) {
        viewModelScope.launch {
            val currentData = (_uiState.value as? UiState.Success)?.data ?: return@launch

            delay(300)
            val newWrittenDates = setOf(
                LocalDate.of(yearMonth.year, yearMonth.month, 3),
                LocalDate.of(yearMonth.year, yearMonth.month, 15),
                LocalDate.of(yearMonth.year, yearMonth.month, 28)
            )

            val stateWithNewDates = currentData.copy(writtenDates = newWrittenDates)
            _uiState.value = UiState.Success(stateWithNewDates)

            loadDataForDate(stateWithNewDates.selectedDate)
        }
    }
}