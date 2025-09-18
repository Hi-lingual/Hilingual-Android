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
    val sortType: WordSortType = WordSortType.Latest,
    val vocaCount: Int = 0,
    val vocaGroupList: UiState<ImmutableList<GroupingVocaModel>> = UiState.Loading,
    val searchKeyword: String = "",
    val searchResultList: ImmutableList<VocaItemModel> = persistentListOf(),
    val vocaItemDetail: UiState<VocaDetailModel> = UiState.Loading,
    val isRefreshing: Boolean = false
)

enum class ScreenType {
    DEFAULT, SEARCH
}
