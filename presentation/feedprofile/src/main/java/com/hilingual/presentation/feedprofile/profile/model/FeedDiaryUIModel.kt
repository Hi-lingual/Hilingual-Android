package com.hilingual.presentation.feedprofile.profile.model

import androidx.compose.runtime.Immutable
import com.hilingual.data.feed.model.FeedProfileModel
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

internal fun SharedDiaryModel.toFeedDiaryUIModel(
    feedProfileModel: FeedProfileModel,
    authorUserId: Long
): FeedDiaryUIModel = FeedDiaryUIModel(
    diaryId = this.diaryId,
    authorUserId = authorUserId,
    authorNickname = feedProfileModel.nickname,
    authorProfileImageUrl = feedProfileModel.profileImageUrl,
    authorStreak = if (feedProfileModel.isMine) null else feedProfileModel.streak,
    sharedDate = this.sharedDate,
    originalText = this.originalText,
    diaryImageUrl = this.diaryImg,
    likeCount = this.likeCount,
    isLiked = this.isLiked,
    isMine = feedProfileModel.isMine
)

internal fun LikedDiaryItemModel.toFeedDiaryUIModel(): FeedDiaryUIModel = FeedDiaryUIModel(
    diaryId = this.diary.diaryId,
    authorUserId = this.profile.userId,
    authorNickname = this.profile.nickname,
    authorProfileImageUrl = this.profile.profileImg,
    authorStreak = if (this.profile.isMine) null else this.profile.streak,
    sharedDate = this.diary.sharedDate,
    originalText = this.diary.originalText,
    diaryImageUrl = this.diary.diaryImg,
    likeCount = this.diary.likeCount,
    isLiked = this.diary.isLiked,
    isMine = this.profile.isMine
)
