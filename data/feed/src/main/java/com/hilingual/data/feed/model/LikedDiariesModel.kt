package com.hilingual.data.feed.model

import com.hilingual.data.feed.dto.response.DiaryDto
import com.hilingual.data.feed.dto.response.LikedDiariesResponseDto
import com.hilingual.data.feed.dto.response.LikedDiaryItemDto
import com.hilingual.data.feed.dto.response.LikedDiaryProfileDto

data class LikedDiariesModel(
    val diaryList: List<FeedItemModel>
)

internal fun LikedDiariesResponseDto.toModel(): LikedDiariesModel = LikedDiariesModel(
    diaryList = this.diaryList.map { it.toFeedItemModel() }
)

internal fun LikedDiaryItemDto.toFeedItemModel(): FeedItemModel = FeedItemModel(
    profile = this.profile.toFeedItemProfileModel(),
    diary = this.diary.toFeedItemDiaryModel()
)

internal fun LikedDiaryProfileDto.toFeedItemProfileModel(): FeedItemProfileModel = FeedItemProfileModel(
    userId = this.userId,
    isMine = this.isMine,
    profileImg = this.profileImg,
    nickname = this.nickname,
    streak = this.streak
)

internal fun DiaryDto.toFeedItemDiaryModel(): FeedItemDiaryModel = FeedItemDiaryModel(
    diaryId = this.diaryId,
    sharedDate = this.sharedDate,
    likeCount = this.likeCount,
    isLiked = this.isLiked,
    diaryImg = this.diaryImg,
    originalText = this.originalText
)
