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
package com.hilingual.presentation.feeddiary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.util.UiState
import com.hilingual.data.diary.model.PhraseBookmarkModel
import com.hilingual.data.diary.repository.DiaryRepository
import com.hilingual.presentation.feeddiary.navigation.FeedDiary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class FeedDiaryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val diaryRepository: DiaryRepository
) : ViewModel() {
    val diaryId = savedStateHandle.toRoute<FeedDiary>().diaryId

    private val _uiState = MutableStateFlow<UiState<FeedDiaryUiState>>(UiState.Success(FeedDiaryUiState.Fake))
    val uiState: StateFlow<UiState<FeedDiaryUiState>> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<FeedDiarySideEffect>()
    val sideEffect: SharedFlow<FeedDiarySideEffect> = _sideEffect.asSharedFlow()

    fun toggleBookmark(phraseId: Long, isMarked: Boolean) {
        viewModelScope.launch {
            diaryRepository.patchPhraseBookmark(
                phraseId = phraseId,
                bookmarkModel = PhraseBookmarkModel(isMarked)
            )
                .onSuccess {
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
                .onLogFailure { }
        }
    }

    fun diaryUnpublish() {
        // TODO: API 호출 성공 후 표시
        viewModelScope.launch {
            _sideEffect.emit(FeedDiarySideEffect.ShowToast(message = "일기가 비공개 되었어요."))
            _sideEffect.emit(FeedDiarySideEffect.NavigateToUp)
        }
    }
}

sealed interface FeedDiarySideEffect {
    data object NavigateToUp : FeedDiarySideEffect
    data class ShowSnackbar(val message: String, val actionLabel: String) : FeedDiarySideEffect
    data class ShowToast(val message: String) : FeedDiarySideEffect
}
