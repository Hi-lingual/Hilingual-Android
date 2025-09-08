package com.hilingual.presentation.feedprofile.profile.model

import androidx.compose.runtime.Immutable
import com.hilingual.data.feed.model.LikedDiaryItemModel
import com.hilingual.data.feed.model.SharedDiaryModel

@Immutable
data class FeedDiaryUIModel(
    val diaryId: Long,
    val authorUserId: Long,
    val authorNickname: String,
    val authorProfileImageUrl: String?,
    val authorStreak: Int?,
    val sharedDate: Long,
    val originalText: String,
    val diaryImageUrl: String?,
    val likeCount: Int,
    val isLiked: Boolean,
    val isMine: Boolean
)

internal fun SharedDiaryModel.toState(
    feedProfileInfoModel: FeedProfileInfoModel,
    authorUserId: Long
): FeedDiaryUIModel = FeedDiaryUIModel(
    diaryId = this.diaryId,
    authorUserId = authorUserId,
    authorNickname = feedProfileInfoModel.nickname,
    authorProfileImageUrl = feedProfileInfoModel.profileImageUrl,
    authorStreak = if (feedProfileInfoModel.isMine) null else feedProfileInfoModel.streak,
    sharedDate = this.sharedDate,
    originalText = this.originalText,
    diaryImageUrl = this.diaryImg,
    likeCount = this.likeCount,
    isLiked = this.isLiked,
    isMine = feedProfileInfoModel.isMine
)

internal fun LikedDiaryItemModel.toState(): FeedDiaryUIModel = FeedDiaryUIModel(
    diaryId = this.diary.diaryId,
    authorUserId = this.profile.userId,
    authorNickname = this.profile.nickname,
    authorProfileImageUrl = this.profile.profileImg,
    authorStreak = this.profile.streak,
    sharedDate = this.diary.sharedDate,
    originalText = this.diary.originalText,
    diaryImageUrl = this.diary.diaryImg,
    likeCount = this.diary.likeCount,
    isLiked = this.diary.isLiked,
    isMine = this.profile.isMine
)
