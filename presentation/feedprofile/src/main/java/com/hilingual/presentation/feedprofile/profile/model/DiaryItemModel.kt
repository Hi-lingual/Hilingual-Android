package com.hilingual.presentation.feedprofile.profile.model

import androidx.compose.runtime.Immutable

internal interface DiaryItem {
    val profileImageUrl: String
    val nickname: String
    val diaryId: Long
    val sharedDate: Long
    val likeCount: Int
    val isLiked: Boolean
    val diaryImageUrl: String?
    val originalText: String
}

@Immutable
data class SharedDiaryItemModel(
    override val profileImageUrl: String,
    override val nickname: String,
    override val diaryId: Long,
    override val sharedDate: Long,
    override val likeCount: Int,
    override val isLiked: Boolean,
    override val diaryImageUrl: String?,
    override val originalText: String
) : DiaryItem

@Immutable
data class LikeDiaryItemModel(
    val userId: Long,
    val streak: Int,
    override val profileImageUrl: String,
    override val nickname: String,
    override val diaryId: Long,
    override val sharedDate: Long,
    override val likeCount: Int,
    override val isLiked: Boolean,
    override val diaryImageUrl: String?,
    override val originalText: String
) : DiaryItem
