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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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

    private var aTozGroupList: ImmutableList<GroupingVocaModel> = persistentListOf()
    private var latestGroupList: ImmutableList<GroupingVocaModel> = persistentListOf()
    private var hasBookmarkChanged = false

    init {
        fetchInitialData()

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

    private fun fetchInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(vocaGroupList = UiState.Loading) }
            loadVocaData()
        }
    }

    private suspend fun loadVocaData(isRefreshing: Boolean = false) {
        coroutineScope {
            val aTozDeferred = async { vocaRepository.getVocaList(sort = WordSortType.AtoZ.sortParam) }
            val latestDeferred = async { vocaRepository.getVocaList(sort = WordSortType.Latest.sortParam) }

            val aTozResult = aTozDeferred.await()
            val latestResult = latestDeferred.await()

            if (aTozResult.isFailure || latestResult.isFailure) {
                aTozResult.onLogFailure {}
                latestResult.onLogFailure {}

                if (isRefreshing) {
                    _uiState.update { it.copy(isRefreshing = false) }
                }
                _sideEffect.emit(VocaSideEffect.ShowErrorDialog(onRetry = ::fetchInitialData))
                return@coroutineScope
            }

            val (count, aTozList) = aTozResult.getOrThrow()
            val (_, latestList) = latestResult.getOrThrow()

            aTozGroupList = aTozList.toImmutableList()
            latestGroupList = latestList.toImmutableList()

            val currentList = getCurrentSortedList()

            _uiState.update {
                it.copy(
                    vocaGroupList = UiState.Success(currentList),
                    vocaCount = count,
                    isRefreshing = false
                )
            }
        }
    }

    private fun getCurrentSortedList(): ImmutableList<GroupingVocaModel> {
        return when (_uiState.value.sortType) {
            WordSortType.AtoZ -> aTozGroupList
            WordSortType.Latest -> latestGroupList
        }
    }

    fun updateSort(sort: WordSortType) {
        if (hasBookmarkChanged) {
            _uiState.update { it.copy(sortType = sort) }
            fetchInitialData()
            hasBookmarkChanged = false
        } else {
            val currentList = when (sort) {
                WordSortType.AtoZ -> aTozGroupList
                WordSortType.Latest -> latestGroupList
            }

            _uiState.update {
                it.copy(
                    sortType = sort,
                    vocaGroupList = UiState.Success(currentList)
                )
            }
        }
    }

    fun fetchVocaDetail(phraseId: Long) {
        viewModelScope.launch {
            vocaRepository.getVocaDetail(phraseId = phraseId)
                .onSuccess { vocaDetail ->
                    _uiState.update {
                        it.copy(vocaItemDetail = UiState.Success(vocaDetail))
                    }
                }
                .onLogFailure {
                    _sideEffect.emit(VocaSideEffect.ShowErrorDialog(onRetry = ::refreshVocaList))
                }
        }
    }

    fun clearSelectedVocaDetail() {
        _uiState.update {
            it.copy(vocaItemDetail = UiState.Loading)
        }
    }

    fun updateSearchKeyword(searchKeyword: String) {
        _uiState.update { it.copy(searchKeyword = searchKeyword) }
    }

    private fun performSearch(searchKeyword: String) {
        _uiState.update { currentState ->
            val filteredList = if (searchKeyword.isNotBlank()) {
                aTozGroupList.flatMap { it.words }
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
                    updateLocalBookmarkState(phraseId, isMarked)
                    hasBookmarkChanged = true
                }
                .onLogFailure {
                    _sideEffect.emit(VocaSideEffect.ShowErrorDialog {})
                }
        }
    }

    private fun updateLocalBookmarkState(phraseId: Long, isMarked: Boolean) {
        aTozGroupList = updateBookmarkInList(aTozGroupList, phraseId, isMarked)
        latestGroupList = updateBookmarkInList(latestGroupList, phraseId, isMarked)

        _uiState.update { currentState ->
            val updatedSearchResults = currentState.searchResultList.map { item ->
                if (item.phraseId == phraseId) {
                    item.copy(isBookmarked = isMarked)
                } else {
                    item
                }
            }.toImmutableList()

            currentState.copy(
                vocaGroupList = UiState.Success(getCurrentSortedList()),
                searchResultList = updatedSearchResults
            )
        }
    }

    private fun updateBookmarkInList(
        list: ImmutableList<GroupingVocaModel>,
        phraseId: Long,
        isMarked: Boolean
    ): ImmutableList<GroupingVocaModel> {
        return list.map { group ->
            group.copy(
                words = group.words.map { item ->
                    if (item.phraseId == phraseId) {
                        item.copy(isBookmarked = isMarked)
                    } else {
                        item
                    }
                }.toImmutableList()
            )
        }.toImmutableList()
    }

    fun refreshVocaList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            loadVocaData(isRefreshing = true)
            hasBookmarkChanged = false
        }
    }
}

sealed interface VocaSideEffect {
    data class ShowErrorDialog(val onRetry: () -> Unit) : VocaSideEffect
}
