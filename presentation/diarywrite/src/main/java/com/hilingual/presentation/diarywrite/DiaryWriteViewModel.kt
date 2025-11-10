/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.presentation.diarywrite

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.util.UiState
import com.hilingual.core.localstorage.DiaryTempManager
import com.hilingual.data.calendar.repository.CalendarRepository
import com.hilingual.data.diary.repository.DiaryRepository
import com.hilingual.presentation.diarywrite.navigation.DiaryWrite
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class DiaryWriteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val calendarRepository: CalendarRepository,
    private val diaryRepository: DiaryRepository,
    private val diaryTempManager: DiaryTempManager
) : ViewModel() {
    private val route: DiaryWrite = savedStateHandle.toRoute<DiaryWrite>()

    private val _uiState = MutableStateFlow(
        DiaryWriteUiState(
            selectedDate = LocalDate.parse(route.selectedDate)
        )
    )
    val uiState: StateFlow<DiaryWriteUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<DiaryWriteSideEffect>()
    val sideEffect: SharedFlow<DiaryWriteSideEffect> = _sideEffect.asSharedFlow()

    private var _feedbackUiState = MutableStateFlow<UiState<Long>>(UiState.Empty)
    val feedbackUiState: StateFlow<UiState<Long>> = _feedbackUiState.asStateFlow()

    private val _hasDiaryTemp = MutableStateFlow(false)
    val hasDiaryTemp: StateFlow<Boolean> = _hasDiaryTemp.asStateFlow()

    init {
        getTopic(route.selectedDate)
        loadDiaryTemp()
    }

    fun updateDiaryText(newText: String) {
        _uiState.update { it.copy(diaryText = newText) }
    }

    fun updateDiaryImageUri(newImageUri: Uri?) {
        _uiState.update { it.copy(diaryImageUri = newImageUri) }
    }

    private fun getTopic(date: String) {
        viewModelScope.launch {
            calendarRepository.getTopic(date)
                .onSuccess { topic ->
                    _uiState.update { it.copy(topicKo = topic.topicKor, topicEn = topic.topicEn) }
                }
                .onLogFailure {
                    _sideEffect.emit(DiaryWriteSideEffect.ShowErrorDialog)
                }
        }
    }

    fun saveDiaryTemp() {
        viewModelScope.launch {
            diaryTempManager.saveDiary(
                selectedDate = uiState.value.selectedDate,
                text = uiState.value.diaryText,
                imageUri = uiState.value.diaryImageUri
            )
            _hasDiaryTemp.value = true

            showToast("임시저장이 완료되었어요.")
            _sideEffect.emit(DiaryWriteSideEffect.NavigateToHome)
        }
    }

    fun loadDiaryTemp() {
        viewModelScope.launch {
            val selectedDate = uiState.value.selectedDate
            val hasDiaryTemp = diaryTempManager.hasDiaryTemp(selectedDate)
            _hasDiaryTemp.value = hasDiaryTemp

            if (hasDiaryTemp) {
                _uiState.update {
                    it.copy(
                        diaryText = diaryTempManager.getDiaryText(selectedDate) ?: "",
                        diaryImageUri = diaryTempManager.getDiaryImageUri(selectedDate)
                            ?.let(Uri::parse)
                    )
                }
            }
        }
    }

    fun postDiaryFeedbackCreate() {
        if (_feedbackUiState.value is UiState.Loading) return

        _feedbackUiState.value = UiState.Loading

        viewModelScope.launch {
            val result = diaryRepository.postDiaryFeedbackCreate(
                originalText = uiState.value.diaryText,
                date = uiState.value.selectedDate,
                imageFileUri = uiState.value.diaryImageUri
            )

            result.onSuccess { response ->
                diaryTempManager.clear(uiState.value.selectedDate)
                _feedbackUiState.update { UiState.Success(response.diaryId) }
            }.onLogFailure { throwable ->
                _feedbackUiState.update { UiState.Failure }
            }
        }
    }

    fun extractTextFromImage(uri: Uri, tempFileToDelete: File? = null) {
        viewModelScope.launch {
            try {
                runCatching {
                    withContext(Dispatchers.IO) {
                        val image = InputImage.fromFilePath(context, uri)
                        val recognizer =
                            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                        recognizer.process(image).await().text
                    }
                }.onSuccess { extractedText ->
                    _uiState.update {
                        it.copy(
                            diaryText = extractedText.take(MAX_DIARY_TEXT_LENGTH)
                        )
                    }
                }.onFailure { throwable ->
                    Timber.tag("DiaryWriteViewModel").e(throwable, "Text recognition failed")
                }
            } finally {
                withContext(Dispatchers.IO) {
                    if (tempFileToDelete?.exists() == true) {
                        tempFileToDelete.delete()
                    }
                }
            }
        }
    }

    companion object {
        private const val MAX_DIARY_TEXT_LENGTH = 1000
    }

    private suspend fun showToast(message: String) {
        _sideEffect.emit(DiaryWriteSideEffect.ShowToast(message = message))
    }
}

sealed interface DiaryWriteSideEffect {
    data object NavigateToHome : DiaryWriteSideEffect
    data object ShowErrorDialog : DiaryWriteSideEffect
    data class ShowToast(val message: String) : DiaryWriteSideEffect
}
