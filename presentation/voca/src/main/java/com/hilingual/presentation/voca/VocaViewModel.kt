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
package com.hilingual.presentation.voca

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.extension.onLogFailure
import com.hilingual.core.common.util.UiState
import com.hilingual.data.diary.model.PhraseBookmarkModel
import com.hilingual.data.diary.repository.DiaryRepository
import com.hilingual.data.voca.model.GroupingVocaModel
import com.hilingual.data.voca.repository.VocaRepository
import com.hilingual.presentation.voca.component.WordSortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VocaViewModel @Inject
constructor(
    private val vocaRepository: VocaRepository,
    private val diaryRepository: DiaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VocaUiState())
    val uiState: StateFlow<VocaUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<VocaSideEffect>()
    val sideEffect: SharedFlow<VocaSideEffect> = _sideEffect.asSharedFlow()

    private var AtoZGroupList: ImmutableList<GroupingVocaModel> = persistentListOf()

    init {
        @OptIn(FlowPreview::class)
        _uiState
            .map { it.searchKeyword }
            .debounce(400L)
            .distinctUntilChanged()
            .onEach { keyword ->
                performSearch(keyword)
            }
            .launchIn(viewModelScope)
    }

    fun updateSort(sort: WordSortType) {
        _uiState.update {
            it.copy(
                sortType = sort,
                vocaGroupList = UiState.Loading
            )
        }
    }

    fun fetchWords(sort: WordSortType, isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isRefresh) {
                _uiState.update { it.copy(isRefreshing = true) }
            }

            vocaRepository.getVocaList(sort = sort.sortParam)
                .onSuccess { (count, vocaLists) ->
                    _uiState.update {
                        it.copy(
                            vocaGroupList = UiState.Success(vocaLists.toImmutableList()),
                            vocaCount = count,
                            isRefreshing = false
                        )
                    }
                    AtoZGroupList = vocaLists.toPersistentList()
                }
                .onLogFailure {
                    _uiState.update { it.copy(isRefreshing = false) }
                    _sideEffect.emit(VocaSideEffect.ShowRetryDialog {})
                }
        }
    }

    fun fetchVocaDetail(phraseId: Long) {
        viewModelScope.launch {
            vocaRepository.getVocaDetail(phraseId = phraseId)
                .onSuccess { vocaDetail ->
                    _uiState.update {
                        it.copy(
                            vocaItemDetail = UiState.Success(vocaDetail)
                        )
                    }
                }
                .onLogFailure {
                    _sideEffect.emit(VocaSideEffect.ShowRetryDialog {})
                }
        }
    }

    fun clearSelectedVocaDetail() {
        _uiState.update {
            it.copy(
                vocaItemDetail = UiState.Loading
            )
        }
    }

    fun updateSearchKeyword(searchKeyword: String) {
        _uiState.update { it.copy(searchKeyword = searchKeyword) }
    }

    private fun performSearch(searchKeyword: String) {
        _uiState.update { currentState ->
            val filteredList = if (searchKeyword.isNotBlank()) {
                AtoZGroupList.flatMap { it.words }
                    .filter { it.phrase.contains(searchKeyword, ignoreCase = true) }
                    .toImmutableList()
            } else {
                persistentListOf()
            }

            currentState.copy(
                searchResultList = filteredList,
                viewType = if (searchKeyword.isBlank()) ScreenType.DEFAULT else ScreenType.SEARCH
            )
        }
    }

    fun clearSearchKeyword() {
        _uiState.update {
            it.copy(
                searchKeyword = "",
                viewType = ScreenType.DEFAULT
            )
        }
    }

    fun toggleBookmark(phraseId: Long, isMarked: Boolean) {
        viewModelScope.launch {
            diaryRepository.patchPhraseBookmark(
                phraseId = phraseId,
                bookmarkModel = PhraseBookmarkModel(isMarked)
            )
                .onSuccess {
                    _uiState.update { currentState ->
                        val updatedGroupList = when (val groupState = currentState.vocaGroupList) {
                            is UiState.Success -> {
                                val updatedList = groupState.data.map { group ->
                                    val updatedWords = group.words.map { item ->
                                        if (item.phraseId == phraseId) {
                                            item.copy(isBookmarked = isMarked)
                                        } else {
                                            item
                                        }
                                    }.toPersistentList()
                                    group.copy(words = updatedWords)
                                }.toPersistentList()

                                UiState.Success(updatedList)
                            }

                            else -> groupState
                        }

                        val updatedSearchResults = currentState.searchResultList.map {
                            if (it.phraseId == phraseId) it.copy(isBookmarked = isMarked) else it
                        }.toPersistentList()

                        currentState.copy(
                            vocaGroupList = updatedGroupList,
                            searchResultList = updatedSearchResults
                        )
                    }
                }
                .onLogFailure {
                    _sideEffect.emit(VocaSideEffect.ShowRetryDialog {})
                }
        }
    }

    fun refreshVocaList() {
        fetchWords(_uiState.value.sortType, isRefresh = true)
    }
}

sealed interface VocaSideEffect {
    data class ShowRetryDialog(val onRetry: () -> Unit) : VocaSideEffect
}
