package com.hilingual.presentation.diarywrite

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.data.calendar.repository.CalendarRepository
import com.hilingual.data.diary.repository.DiaryRepository
import com.hilingual.presentation.diarywrite.component.DiaryFeedbackState
import com.hilingual.presentation.diarywrite.navigation.DiaryWrite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
internal class DiaryWriteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val calendarRepository: CalendarRepository,
    private val diaryRepository: DiaryRepository
) : ViewModel() {
    private val route: DiaryWrite = savedStateHandle.toRoute<DiaryWrite>()

    private val _uiState = MutableStateFlow(
        DiaryWriteUiState(
            selectedDate = LocalDate.parse(route.selectedDate)
        )
    )
    val uiState: StateFlow<DiaryWriteUiState> = _uiState.asStateFlow()

    private var _feedbackState: MutableStateFlow<DiaryFeedbackState> =
        MutableStateFlow(DiaryFeedbackState.Default)
    val feedbackState: StateFlow<DiaryFeedbackState> = _feedbackState.asStateFlow()

    init {
        getTopic(route.selectedDate)
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
    fun postDiaryFeedbackCreate() {
        // TODO: date fomatting 어디에서 할건지 고민해보기!!
        val date = uiState.value.selectedDate.format(DateTimeFormatter.ISO_DATE)

        _feedbackState.value = DiaryFeedbackState.Loading

        viewModelScope.launch {
            diaryRepository.postDiaryFeedbackCreate(
                originalText = uiState.value.diaryText,
                date = uiState.value.selectedDate,
                imageFileUri = uiState.value.diaryImageUri
            ).onSuccess { response ->
                _feedbackState.update { DiaryFeedbackState.Complete(response.diaryId) }
            }.onLogFailure { throwable ->
                _feedbackState.value = DiaryFeedbackState.Failure(throwable)
            }
        }
    }
}
