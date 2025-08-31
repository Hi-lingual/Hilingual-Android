package com.hilingual.presentation.feeddiary.model

import androidx.compose.runtime.Immutable

@Immutable
data class ProfileContentUiModel(
    val userId: Long,
    val profileUrl: String,
    val nickname: String,
    val streak: Int,
    val isLiked: Boolean,
    val likeCount: Int,
    val sharedDateInMinutes: Long
)
