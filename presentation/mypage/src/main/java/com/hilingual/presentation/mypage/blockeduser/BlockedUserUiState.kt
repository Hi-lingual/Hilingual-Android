package com.hilingual.presentation.mypage.blockeduser

import androidx.compose.runtime.Immutable
import com.hilingual.core.common.util.UiState
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class BlockedUserUiState(
    val blockedUserList: UiState<ImmutableList<BlockedUserUiModel>> = UiState.Loading
)
