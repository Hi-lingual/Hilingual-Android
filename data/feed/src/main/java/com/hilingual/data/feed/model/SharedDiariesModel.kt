package com.hilingual.data.feed.model

import com.hilingual.data.feed.dto.response.DiaryDto
import com.hilingual.data.feed.dto.response.SharedDiariesResponseDto
import com.hilingual.data.feed.dto.response.SharedDiaryProfileDto
import javax.annotation.concurrent.Immutable

@Immutable
data class SharedDiariesModel(
    val profile: ProfileModel,
    val diaryList: List<SharedDiaryModel>
)

@Immutable
data class ProfileModel(
    val profileImg: String?,
    val nickname: String
)

@Immutable
data class SharedDiaryModel(
    val diaryId: Long,
    val sharedDate: Long,
    val likeCount: Int,
    val isLiked: Boolean,
    val diaryImg: String?,
    val originalText: String
)

internal fun SharedDiariesResponseDto.toModel(): SharedDiariesModel = SharedDiariesModel(
    profile = this.profile.toModel(),
    diaryList = this.diaryList.map { it.toSharedDiaryModel() }
)

internal fun SharedDiaryProfileDto.toModel(): ProfileModel = ProfileModel(
    profileImg = this.profileImg,
    nickname = this.nickname
)

internal fun DiaryDto.toSharedDiaryModel(): SharedDiaryModel = SharedDiaryModel(
    diaryId = this.diaryId,
    sharedDate = this.sharedDate,
    likeCount = this.likeCount,
    isLiked = this.isLiked,
    diaryImg = this.diaryImg,
    originalText = this.originalText
)
