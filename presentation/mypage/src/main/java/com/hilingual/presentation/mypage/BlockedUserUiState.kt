package com.hilingual.presentation.mypage

import androidx.compose.runtime.Immutable

@Immutable
data class BlockedUserUiState(
    val userId: Long,
    val profileImageUrl: String,
    val nickname: String,
    val isUnblocked: Boolean = false
)
