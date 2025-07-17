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
import com.hilingual.presentation.diarywrite.util.TextRecognitionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class DiaryWriteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val calendarRepository: CalendarRepository,
    private val diaryRepository: DiaryRepository,
    private val textRecognitionManager: TextRecognitionManager
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

    fun extractTextFromImage(uri: Uri) {
        viewModelScope.launch {
            try {
                val extractedText = textRecognitionManager.recognizeTextFromImage(uri)

                val filteredText = if (extractedText.length > 1000) {
                    extractedText.substring(0, 1000)
                } else {
                    extractedText
                }

                _uiState.update { it.copy(diaryText = filteredText) }
            } catch (e: Exception) {
                Timber.tag("DiaryWriteViewModel").e(e, "Text recognition failed")
            }
        }
    }
}
