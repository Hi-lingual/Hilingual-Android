package com.hilingual.presentation.voca

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilingual.core.common.util.UiState
import com.hilingual.presentation.voca.component.WordSortType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class VocaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<VocaUiState>>(UiState.Loading)
    val uiState: StateFlow<UiState<VocaUiState>> = _uiState.asStateFlow()

    private var originalAtoZGroupList = listOf(
        VocaGroup(
            "A", listOf(
                VocaItem(1, "amazing", listOf("형용사")),
                VocaItem(2, "apple", listOf("명사"))
            )
        ),
        VocaGroup(
            "B", listOf(
                VocaItem(3, "banana", listOf("동사"))
            )
        ),
        VocaGroup(
            "E", listOf(
                VocaItem(4, "epiphany", listOf("명사")),
                VocaItem(5, "example", listOf("명사"))
            )
        )
    )

    private var originalLatestGroupList = listOf(
        VocaGroup(
            "today", listOf(
                VocaItem(10, "come across as", listOf("동사", "숙어")),
                VocaItem(9, "underwhelming", listOf("형용사")),
                VocaItem(8, "spacious", listOf("형용사")),
                VocaItem(7, "oversleep", listOf("동사"))
            )
        ),
        VocaGroup(
            "7days", listOf(
                VocaItem(6, "resonate with", listOf("동사", "구")),
                VocaItem(5, "rekindle my interest", listOf("동사", "구")),
                VocaItem(4, "emotional vulnerability", listOf("명사", "구")),
                VocaItem(3, "out of the blue", listOf("부사")),
                VocaItem(2, "short-lived but impactful", listOf("형용사", "구")),
                VocaItem(1, "ghost", listOf("명사")),
                VocaItem(0, "run late", listOf("동사", "숙어"))
            )
        )
    )

    init {
        fetchWords(WordSortType.Latest)
    }

    fun fetchWords(sort: WordSortType) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {

            val source = when (sort) {
                WordSortType.AtoZ -> originalAtoZGroupList
                WordSortType.Latest -> originalLatestGroupList
            }

            val filtered = source.map { group ->
                group.copy(words = group.words.filter { it.isBookmarked })
            }.filter { it.words.isNotEmpty() }

            _uiState.value = UiState.Success(
                VocaUiState(
                    sortType = sort,
                    vocaGroupList = filtered,
                    isLoading = false
                )
            )
        }
    }

    fun toggleBookmark(item: VocaItem) {
        fun updateBookmark(source: List<VocaGroup>) = source.map { group ->
            group.copy(words = group.words.map {
                if (it.phraseId == item.phraseId) it.copy(isBookmarked = !it.isBookmarked) else it
            })
        }

        when ((uiState.value as? UiState.Success)?.data?.sortType) {
            WordSortType.Latest -> originalLatestGroupList = updateBookmark(originalLatestGroupList)
            WordSortType.AtoZ -> originalAtoZGroupList = updateBookmark(originalAtoZGroupList)
            else -> return
        }

        val currentState = uiState.value as? UiState.Success ?: return
        val updatedGroups = currentState.data.vocaGroupList.map { group ->
            group.copy(words = group.words.map {
                if (it.phraseId == item.phraseId) it.copy(isBookmarked = !it.isBookmarked) else it
            })
        }
        _uiState.value = UiState.Success(currentState.data.copy(vocaGroupList = updatedGroups))
    }
}