package com.hilingual.data.feed.model

import com.hilingual.data.feed.dto.response.DiaryDto
import com.hilingual.data.feed.dto.response.LikedDiariesResponseDto
import com.hilingual.data.feed.dto.response.LikedDiaryItemDto
import com.hilingual.data.feed.dto.response.LikedDiaryProfileDto

data class LikedDiariesModel(
    val diaryList: List<LikedDiaryItemModel>
)

data class LikedDiaryItemModel(
    val profile: LikedDiaryProfileModel,
    val diary: LikedDiaryModel
)

data class LikedDiaryProfileModel(
    val userId: Long,
    val isMine: Boolean,
    val profileImg: String?,
    val nickname: String,
    val streak: Int
)

data class LikedDiaryModel(
    val diaryId: Long,
    val sharedDate: Long,
    val likeCount: Int,
    val isLiked: Boolean,
    val diaryImg: String?,
    val originalText: String
)

internal fun LikedDiariesResponseDto.toModel(): LikedDiariesModel = LikedDiariesModel(
    diaryList = this.diaryList.map { it.toModel() }
)

internal fun LikedDiaryItemDto.toModel(): LikedDiaryItemModel = LikedDiaryItemModel(
    profile = this.profile.toModel(),
    diary = this.diary.toLikedDiaryModel()
)

internal fun LikedDiaryProfileDto.toModel(): LikedDiaryProfileModel = LikedDiaryProfileModel(
    userId = this.userId,
    isMine = this.isMine,
    profileImg = this.profileImg,
    nickname = this.nickname,
    streak = this.streak
)

internal fun DiaryDto.toLikedDiaryModel(): LikedDiaryModel = LikedDiaryModel(
    diaryId = this.diaryId,
    sharedDate = this.sharedDate,
    likeCount = this.likeCount,
    isLiked = this.isLiked,
    diaryImg = this.diaryImg,
    originalText = this.originalText
)
