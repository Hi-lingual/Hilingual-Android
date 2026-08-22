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
import com.hilingual.core.common.model.LoadErrorHandleAction
import com.hilingual.core.common.util.UiState
import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.core.common.util.toLocalDateOrNull
import com.hilingual.core.common.widget.WidgetUpdater
import com.hilingual.core.navigation.DiaryWriteMode
import com.hilingual.data.calendar.repository.CalendarRepository
import com.hilingual.data.diary.repository.DiaryLocalRepository
import com.hilingual.data.diary.repository.DiaryRepository
import com.hilingual.data.diary.repository.TextRecognitionRepository
import com.hilingual.presentation.diarywrite.navigation.DiaryWrite
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
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

@HiltViewModel
internal class DiaryWriteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val calendarRepository: CalendarRepository,
    private val diaryRepository: DiaryRepository,
    private val diaryLocalRepository: DiaryLocalRepository,
    private val textRecognitionRepository: TextRecognitionRepository,
    private val widgetUpdater: WidgetUpdater,
) : ViewModel() {
    private val route: DiaryWrite = savedStateHandle.toRoute<DiaryWrite>()

    private val _uiState = MutableStateFlow(
        DiaryWriteUiState(
            selectedDate = requireNotNull(route.selectedDate.toLocalDateOrNull()) {
                "Invalid selectedDate: ${route.selectedDate}"
            },
            isRecovery = route.mode == DiaryWriteMode.RECOVERY,
        ),
    )
    val uiState: StateFlow<DiaryWriteUiState> = _uiState.asStateFlow()

    private val _feedbackUiState = MutableStateFlow<UiState<Long>>(UiState.Empty)
    val feedbackUiState: StateFlow<UiState<Long>> = _feedbackUiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<DiaryWriteSideEffect>()
    val sideEffect: SharedFlow<DiaryWriteSideEffect> = _sideEffect.asSharedFlow()

    init {
        fetchTopic()

        if (route.mode == DiaryWriteMode.DEFAULT) {
            loadDiaryTemp()
        }
    }

    fun updateDiaryText(newText: String) {
        _uiState.update { it.copy(diaryText = newText) }
    }

    fun updateDiaryImageUri(newImageUri: Uri?) {
        _uiState.update { it.copy(diaryImageUri = newImageUri) }
    }

    fun saveDiaryTemp() {
        viewModelScope.launch {
            val state = uiState.value

            diaryLocalRepository.saveDiary(
                selectedDate = state.selectedDate,
                text = state.diaryText,
                imageUri = state.diaryImageUri,
            )
                .onSuccess {
                    _sideEffect.emit(DiaryWriteSideEffect.ShowToast("임시저장이 완료되었어요."))
                    _sideEffect.emit(DiaryWriteSideEffect.NavigateToHome)
                }
                .onLogFailure { }
        }
    }

    fun requestDiaryFeedback(): Boolean {
        if (_feedbackUiState.value is UiState.Loading) return false
        _feedbackUiState.value = UiState.Loading

        viewModelScope.launch {
            val state = uiState.value

            val result = if (route.mode == DiaryWriteMode.RECOVERY) {
                diaryRepository.postDiaryRecoveryCreate(
                    originalText = state.diaryText,
                    date = state.selectedDate,
                    imageFileUri = state.diaryImageUri,
                )
            } else {
                diaryRepository.postDiaryFeedbackCreate(
                    originalText = state.diaryText,
                    date = state.selectedDate,
                    imageFileUri = state.diaryImageUri,
                )
            }

            result
                .onSuccess { response ->
                    diaryLocalRepository.clearDiaryTemp(state.selectedDate)
                    _feedbackUiState.update { UiState.Success(response.diaryId) }
                    suspendRunCatching { widgetUpdater.updateAll() }
                        .onLogFailure { }
                }
                .onLogFailure {
                    _feedbackUiState.update { UiState.Failure(LoadErrorHandleAction.Retry) }
                }
        }

        return true
    }

    fun returnToWriting() {
        _feedbackUiState.update { UiState.Empty }
    }

    fun extractTextFromImage(imageUri: Uri, tempImageFile: File? = null) {
        viewModelScope.launch {
            try {
                textRecognitionRepository.extractTextFromImage(imageUri)
                    .onSuccess { extractedText ->
                        _uiState.update {
                            it.copy(diaryText = extractedText.take(MAX_DIARY_TEXT_LENGTH))
                        }
                    }
                    .onLogFailure { }
            } finally {
                withContext(Dispatchers.IO) {
                    if (tempImageFile?.exists() == true) {
                        tempImageFile.delete()
                    }
                }
            }
        }
    }

    private fun fetchTopic() {
        viewModelScope.launch {
            calendarRepository.getTopic(uiState.value.selectedDate)
                .onSuccess { topic ->
                    _uiState.update { it.copy(topicKo = topic.topicKor, topicEn = topic.topicEn) }
                }
                .onLogFailure {
                    _uiState.update { it.copy(topicKo = "", topicEn = "") }
                }
        }
    }

    private fun loadDiaryTemp() {
        viewModelScope.launch {
            val selectedDate = uiState.value.selectedDate

            val isDiaryTempExist = diaryLocalRepository.isDiaryTempExist(selectedDate)
                .onLogFailure { }
                .getOrDefault(false)

            _uiState.update { it.copy(isDiaryTempExist = isDiaryTempExist) }
            if (!isDiaryTempExist) return@launch

            diaryLocalRepository.getDiaryText(selectedDate)
                .onSuccess { text ->
                    _uiState.update {
                        it.copy(
                            diaryText = text.orEmpty(),
                            initialDiaryText = text.orEmpty(),
                        )
                    }
                }
                .onLogFailure { }

            diaryLocalRepository.getDiaryImageUri(selectedDate)
                .onSuccess { imageUri ->
                    val uri = imageUri?.let(Uri::parse)
                    _uiState.update {
                        it.copy(
                            diaryImageUri = uri,
                            initialDiaryImageUri = uri,
                        )
                    }
                }
                .onLogFailure { }
        }
    }
}

internal sealed interface DiaryWriteSideEffect {
    data object NavigateToHome : DiaryWriteSideEffect
    data object ShowErrorDialog : DiaryWriteSideEffect
    data class ShowToast(val message: String) : DiaryWriteSideEffect
}
