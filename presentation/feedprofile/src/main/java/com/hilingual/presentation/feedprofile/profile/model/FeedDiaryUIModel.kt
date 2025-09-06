package com.hilingual.presentation.feedprofile.profile.model

import androidx.compose.runtime.Immutable
import com.hilingual.data.feed.model.FeedProfileModel
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
