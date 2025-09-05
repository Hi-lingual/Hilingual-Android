package com.hilingual.presentation.mypage

import androidx.compose.runtime.Immutable

@Immutable
internal data class MyPageUiModel(
    val profileImageUrl: String = "",
    val profileNickname: String = "",
    val profileProvider: String = ""
)
