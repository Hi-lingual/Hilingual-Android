package com.hilingual.presentation.diarywrite

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.data.calendar.repository.CalendarRepository
import com.hilingual.presentation.diarywrite.component.DiaryFeedbackState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class DiaryWriteViewModel @Inject constructor(
    private val calendarRepository: CalendarRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiaryWriteUiState())
    val uiState: StateFlow<DiaryWriteUiState> = _uiState.asStateFlow()

    private var _feedbackState: MutableStateFlow<DiaryFeedbackState> =
        MutableStateFlow(DiaryFeedbackState.Default)
    val feedbackState: StateFlow<DiaryFeedbackState> = _feedbackState.asStateFlow()

    fun updateSelectedDate(newDate: LocalDate) {
        _uiState.update { it.copy(selectedDate = newDate) }
    }

    fun updateTopic(newTopicKo: String, newTopicEn: String) {
        _uiState.update { it.copy(topicKo = newTopicKo, topicEn = newTopicEn) }
    }

    fun updateDiaryText(newText: String) {
        _uiState.update { it.copy(diaryText = newText) }
    }

    fun updateDiaryImageUri(newImageUri: Uri?) {
        _uiState.update { it.copy(diaryImageUri = newImageUri) }
    }

    // TODO: 일기 피드백 요청 POST API 관련 함수
    fun getTopic(date: String) {
        viewModelScope.launch {
            calendarRepository.getTopic(date)
                .onSuccess { topic ->
                    _uiState.update { it.copy(topicKo = topic.topicKor, topicEn = topic.topicEn) }
                }
                .onLogFailure { }
        }
    }
}
