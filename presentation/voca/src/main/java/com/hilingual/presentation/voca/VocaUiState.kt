package com.hilingual.presentation.voca

import androidx.compose.runtime.Immutable
import com.hilingual.core.common.util.UiState
import com.hilingual.data.voca.model.GroupingVocaModel
import com.hilingual.data.voca.model.VocaDetailModel
import com.hilingual.data.voca.model.VocaItemModel
import com.hilingual.presentation.voca.component.WordSortType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class VocaUiState(
    val viewType: ScreenType = ScreenType.DEFAULT,
    val sortType: WordSortType = WordSortType.AtoZ,
    val vocaCount: Int = 0,
    val vocaGroupList: UiState<ImmutableList<GroupingVocaModel>> = UiState.Loading,
    val searchKeyword: String = "",
    val searchResultList: ImmutableList<VocaItemModel> = persistentListOf(),
    val vocaItemDetail: UiState<VocaDetailModel> = UiState.Loading
)

enum class ScreenType {
    DEFAULT, SEARCH
}
