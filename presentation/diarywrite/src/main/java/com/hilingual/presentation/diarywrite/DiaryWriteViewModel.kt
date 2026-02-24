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

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.util.UiState
import com.hilingual.core.navigation.DiaryWriteMode
import com.hilingual.data.calendar.repository.CalendarRepository
import com.hilingual.data.diary.localstorage.DiaryTempRepository
import com.hilingual.data.diary.repository.DiaryRepository
import com.hilingual.data.diary.repository.TextRecognitionRepository
import com.hilingual.presentation.diarywrite.navigation.DiaryWrite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class DiaryWriteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val calendarRepository: CalendarRepository,
    private val diaryRepository: DiaryRepository,
    private val diaryTempRepository: DiaryTempRepository,
    private val textRecognitionRepository: TextRecognitionRepository
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

    init {
        getTopic(route.selectedDate)

        when (route.mode) {
            DiaryWriteMode.DEFAULT -> loadDiaryTemp()
            DiaryWriteMode.NEW -> {
                // Do nothing, start new diary to.Dalji
            }
        }
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
                    _uiState.update { it.copy(topicKo = "", topicEn = "") }
                }
        }
    }

    fun handleDiaryTempSavingFlow() {
        viewModelScope.launch {
            diaryTempRepository.saveDiary(
                selectedDate = uiState.value.selectedDate,
                text = uiState.value.diaryText,
                imageUri = uiState.value.diaryImageUri
            )
                .onSuccess {
                    _uiState.update { it.copy(isDiaryTempExist = true) }
                    handleDiaryTempSaved()
                }
                .onLogFailure { }
        }
    }

    private suspend fun handleDiaryTempSaved() {
        showToast("임시저장이 완료되었어요.")
        _sideEffect.emit(DiaryWriteSideEffect.NavigateToHome)
    }

    fun loadDiaryTemp() {
        viewModelScope.launch {
            val selectedDate = uiState.value.selectedDate

            diaryTempRepository.isDiaryTempExist(selectedDate)
                .onSuccess { isDiaryTempExist ->
                    if (!isDiaryTempExist) {
                        _uiState.update { it.copy(isDiaryTempExist = false) }
                        return@launch
                    }

                    _uiState.update { it.copy(isDiaryTempExist = true) }

                    diaryTempRepository.getDiaryText(selectedDate)
                        .onSuccess { text ->
                            _uiState.update {
                                it.copy(
                                    diaryText = text ?: "",
                                    initialDiaryText = text ?: ""
                                )
                            }
                        }
                        .onLogFailure { }

                    diaryTempRepository.getDiaryImageUri(selectedDate)
                        .onSuccess { imageUri ->
                            val uri = imageUri?.let(Uri::parse)
                            _uiState.update {
                                it.copy(
                                    diaryImageUri = uri,
                                    initialDiaryImageUri = uri
                                )
                            }
                        }
                }
                .onLogFailure { }
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
                diaryTempRepository.clearDiaryTemp(uiState.value.selectedDate)
                _feedbackUiState.update { UiState.Success(response.diaryId) }
            }.onLogFailure { throwable ->
                _feedbackUiState.update { UiState.Failure }
            }
        }
    }

    fun extractTextFromImage(uri: Uri, tempFileToDelete: File? = null) {
        viewModelScope.launch {
            try {
                textRecognitionRepository.extractTextFromImage(uri)
                    .onSuccess { extractedText ->
                        _uiState.update {
                            it.copy(
                                diaryText = extractedText.take(MAX_DIARY_TEXT_LENGTH)
                            )
                        }
                    }
                    .onLogFailure { }
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
