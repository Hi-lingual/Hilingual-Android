package com.hilingual.data.feed.model

import com.hilingual.data.feed.dto.response.DiaryProfileResponseDto

data class FeedDiaryProfileModel(
    val isMine: Boolean,
    val profile: ProfileModel,
    val diary: DiaryInfoModel
)

data class ProfileModel(
    val userId: Long,
    val profileImg: String?,
    val nickname: String,
    val streak: Int
)

data class DiaryInfoModel(
    val sharedDate: Long,
    val likeCount: Int,
    val isLiked: Boolean
)

internal fun DiaryProfileResponseDto.toModel() = FeedDiaryProfileModel(
    isMine = this.isMine,
    profile = with(this.profile) {
        ProfileModel(
            userId = userId,
            profileImg = profileImg,
            nickname = nickname,
            streak = streak
        )
    },
    diary = with(this.diary) {
        DiaryInfoModel(
            sharedDate = sharedDate,
            likeCount = likeCount,
            isLiked = isLiked
        )
    }
)
