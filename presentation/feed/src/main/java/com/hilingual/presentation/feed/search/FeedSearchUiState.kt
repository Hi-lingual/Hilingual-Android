package com.hilingual.presentation.feed.search

import androidx.compose.runtime.Immutable
import com.hilingual.core.common.util.UiState
import com.hilingual.presentation.feed.model.UserSearchUiModel
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class FeedSearchUiState(
    val searchWord: String = "",
    val searchResultUserList: UiState<ImmutableList<UserSearchUiModel>> = UiState.Empty
)
