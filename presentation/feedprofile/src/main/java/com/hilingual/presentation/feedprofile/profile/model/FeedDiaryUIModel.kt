package com.hilingual.presentation.feedprofile.profile.model

import androidx.compose.runtime.Immutable

@Immutable
data class FeedDiaryUIModel(
    val diaryId: Long,
    val authorUserId: Long,
    val authorNickname: String,
    val authorProfileImageUrl: String,
    val authorStreak: Int,
    val sharedDate: Long,
    val originalText: String,
    val diaryImageUrl: String?,
    val likeCount: Int,
    val isLiked: Boolean,
    val isMine: Boolean
)
