package com.hilingual.presentation.voca

import androidx.compose.runtime.Immutable
import com.hilingual.presentation.voca.component.WordSortType

@Immutable
internal data class  VocaGroup(
    val group: String,
    val words: List<VocaItem>
)

@Immutable
internal data class  VocaItem(
    val phraseId: Int,
    val phrase: String,
    val phraseType: List<String>,
    val isBookmarked: Boolean = true
)

@Immutable
internal data class VocaUiState(
    val sortType: WordSortType = WordSortType.Latest,
    val vocaGroupList: List<VocaGroup> = emptyList(),
    val isLoading: Boolean = true
)