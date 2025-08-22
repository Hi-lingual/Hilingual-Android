package com.hilingual.presentation.feedprofile.model

import androidx.compose.runtime.Immutable

@Immutable
data class SharedDiaryItemModel(
    val profileImageUrl: String,
    val nickname: String,
    val diaryId: Long,
    val sharedDate: Long,
    val likeCount: Int,
    val isLiked: Boolean,
    val diaryImgUrl: String?,
    val originalText: String,
)