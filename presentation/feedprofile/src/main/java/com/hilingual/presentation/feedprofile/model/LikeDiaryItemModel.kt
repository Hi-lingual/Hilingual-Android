package com.hilingual.presentation.feedprofile.model

import androidx.compose.runtime.Immutable

@Immutable
data class LikeDiaryItemModel(
    val userId: Long,
    val isMine: Boolean,
    val profileImageUrl: String,
    val nickname: String,
    val streak: Int,
    val diaryId: Long,
    val sharedDate: Long,
    val likeCount: Int,
    val isLiked: Boolean,
    val diaryImgUrl: String?,
    val originalText: String
)
