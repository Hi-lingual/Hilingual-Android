package com.hilingual.presentation.diarywrite

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.data.diary.repository.DiaryRepository
import com.hilingual.presentation.diarywrite.component.DiaryFeedbackState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
internal class DiaryWriteViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiaryWriteUiState())
    val uiState: StateFlow<DiaryWriteUiState> = _uiState.asStateFlow()

    private var _feedbackState = MutableStateFlow<DiaryFeedbackState>(DiaryFeedbackState.Default)
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
    fun postDiaryFeedbackCreate(context: Context) {
        val originalText = uiState.value.diaryText
        val date = uiState.value.selectedDate.format(DateTimeFormatter.ISO_DATE)
        val imageFile = uiState.value.diaryImageUri?.toFile(context)

        _feedbackState.value = DiaryFeedbackState.Loading

        viewModelScope.launch {
            diaryRepository.postDiaryFeedbackCreate(
                originalText = originalText,
                date = date,
                imageFile = imageFile
            ).onSuccess { response ->
                _feedbackState.update { DiaryFeedbackState.Complete(response.diaryId) }
            }.onLogFailure { throwable ->
                _feedbackState.value = DiaryFeedbackState.Failure(throwable)
            }
        }
    }

    private fun Uri.toFile(context: Context): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(this) ?: return null
            val tempFile = File.createTempFile("diary_image", ".jpg", context.cacheDir)
            tempFile.outputStream().use { output ->
                inputStream.copyTo(output)
            }
            tempFile
        } catch (e: Exception) {
            null
        }
    }
}
