package com.hilingual.presentation.mypage

import androidx.compose.runtime.Immutable

@Immutable
internal data class MyPageUiState(
    val profileImageUrl: String = "",
    val profileNickname: String = "",
    val profileProvider: String = ""
)
