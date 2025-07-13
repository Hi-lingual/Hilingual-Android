package com.hilingual.presentation.voca

import androidx.lifecycle.ViewModel
import com.hilingual.core.common.util.UiState
import com.hilingual.presentation.voca.component.WordSortType
import com.hilingual.presentation.voca.model.VocaGroup
import com.hilingual.presentation.voca.model.VocaItem
import com.hilingual.presentation.voca.model.VocaItemDetail
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class VocaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<VocaUiState>(VocaUiState())
    val uiState: StateFlow<VocaUiState> = _uiState.asStateFlow()

    private val vocaDetailItem =
        VocaItemDetail(0, "run late", persistentListOf("동사", "숙어"), "늦다", "2025.06.12")

    private var originalAtoZGroupList = persistentListOf(
        VocaGroup(
            "A",
            persistentListOf(
                VocaItem(1, "amazing", persistentListOf("형용사")),
                VocaItem(2, "apple", persistentListOf("명사"))
            )
        ),
        VocaGroup(
            "B",
            persistentListOf(
                VocaItem(3, "banana", persistentListOf("동사"))
            )
        ),
        VocaGroup(
            "E",
            persistentListOf(
                VocaItem(4, "epiphany", persistentListOf("명사")),
                VocaItem(5, "example", persistentListOf("명사"))
            )
        )
    )

    private var originalLatestGroupList = persistentListOf<VocaGroup>()

    init {
        fetchWords(WordSortType.Latest)
    }

    fun fetchWords(sort: WordSortType) {
        val source = when (sort) {
            WordSortType.AtoZ -> originalAtoZGroupList
            WordSortType.Latest -> originalLatestGroupList
        }

        _uiState.update {
            it.copy(
                vocaGroupList = UiState.Success(source),
                sortType = sort
            )
        }
    }

    fun fetchVocaDetail(phraseId: Long) {
        _uiState.update {
            it.copy(
                vocaItemDetail = UiState.Success(vocaDetailItem)
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

    fun toggleBookmark(phraseId: Long) {
    }

    fun clearSelectedVocaDetail() {
        _uiState.update {
            it.copy(
                vocaItemDetail = UiState.Loading
            )
        }
    }

    fun updateSearchKeywordAndSearch(searchKeyword: String) {
        _uiState.update {
            it.copy(
                searchKeyword = searchKeyword,
                viewType = if (searchKeyword.isBlank()) {
                    ScreenType.DEFAULT
                } else {
                    ScreenType.SEARCH
                }
            )
        }
    }
}
