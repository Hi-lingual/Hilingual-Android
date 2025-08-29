package com.hilingual.presentation.mypage.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class BlockedUserUiModel(
    val userId: Long,
    val profileImageUrl: String,
    val nickname: String,
    val isBlocked: Boolean = true
)