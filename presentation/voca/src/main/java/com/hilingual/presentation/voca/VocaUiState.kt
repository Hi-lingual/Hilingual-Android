package com.hilingual.presentation.voca

import androidx.compose.runtime.Immutable
import com.hilingual.core.common.util.UiState
import com.hilingual.presentation.voca.component.WordSortType
import com.hilingual.presentation.voca.model.VocaGroup
import com.hilingual.presentation.voca.model.VocaItem
import com.hilingual.presentation.voca.model.VocaItemDetail
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class VocaUiState(
    val viewType: ScreenType = ScreenType.DEFAULT,
    val sortType: WordSortType = WordSortType.Latest,
    val vocaCount: Int = 0,
    val vocaGroupList: UiState<ImmutableList<VocaGroup>> = UiState.Loading,
    val searchKeyword: String = "",
    val searchResultList: UiState<ImmutableList<VocaItem>> = UiState.Loading,
    val vocaItemDetail: UiState<VocaItemDetail> = UiState.Loading

)

enum class ScreenType {
    DEFAULT, SEARCH
}