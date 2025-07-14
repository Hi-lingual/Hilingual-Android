package com.hilingual.presentation.voca

import androidx.compose.runtime.Immutable
import com.hilingual.core.common.util.UiState
import com.hilingual.data.voca.model.VocaDetail
import com.hilingual.data.voca.model.VocaItem
import com.hilingual.data.voca.model.VocaList
import com.hilingual.presentation.voca.component.WordSortType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class VocaUiState(
    val viewType: ScreenType = ScreenType.DEFAULT,
    val sortType: WordSortType = WordSortType.Latest,
    val vocaCount: Int = 0,
    val vocaGroupList: UiState<ImmutableList<VocaList>> = UiState.Loading,
    val searchKeyword: String = "",
    val searchResultList: ImmutableList<VocaItem> = persistentListOf(),
    val vocaItemDetail: UiState<VocaDetail> = UiState.Loading
)

enum class ScreenType {
    DEFAULT, SEARCH
}
