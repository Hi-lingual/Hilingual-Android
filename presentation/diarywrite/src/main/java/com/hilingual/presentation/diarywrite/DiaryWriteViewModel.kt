package com.hilingual.presentation.diarywrite

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class DiaryWriteViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(DiaryWriteUiState())
    val uiState: StateFlow<DiaryWriteUiState> = _uiState.asStateFlow()

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
}