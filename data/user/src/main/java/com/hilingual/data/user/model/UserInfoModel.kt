package com.hilingual.data.user.model

import com.hilingual.data.user.dto.reponse.UserInfoResponseDto

data class UserInfoModel(
    val nickname: String,
    val profileImg: String,
    val streak: Int,
    val totalDiaries: Int
)

internal fun UserInfoResponseDto.toModel() = UserInfoModel(
    nickname = this.nickname,
    profileImg = this.profileImg,
    streak = this.streak,
    totalDiaries = this.totalDiaries
)
