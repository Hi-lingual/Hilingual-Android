package com.hilingual.presentation.feed.model

internal data class FeedPreviewUiModel(
    val userId: Long,
    val profileUrl: String,
    val nickname: String,
    val streak: Int,
    val sharedDateInMinutes: Long,
    val content: String,
    val imageUrl: String?,
    val diaryId: Long,
    val likeCount: Int,
    val isLiked: Boolean,
)