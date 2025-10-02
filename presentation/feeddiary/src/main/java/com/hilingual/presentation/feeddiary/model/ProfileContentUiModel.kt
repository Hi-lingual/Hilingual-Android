package com.hilingual.presentation.feeddiary.model

import androidx.compose.runtime.Immutable

@Immutable
data class ProfileContentUiModel(
    val userId: Long = 0,
    val profileUrl: String = "",
    val nickname: String = "UserName",
    val streak: Int = 0,
    val isLiked: Boolean = false,
    val likeCount: Int = 0,
    val sharedDateInMinutes: Long = 0
)
