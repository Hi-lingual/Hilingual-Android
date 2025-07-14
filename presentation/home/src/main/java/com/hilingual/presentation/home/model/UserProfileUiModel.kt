package com.hilingual.presentation.home.model

import androidx.compose.runtime.Immutable
import com.hilingual.data.user.model.UserInfoModel

@Immutable
data class UserProfileUiModel(
    val nickname: String = "",
    val profileImg: String = "",
    val totalDiaries: Int = 0,
    val streak: Int = 0
)

fun UserInfoModel.toState() = UserProfileUiModel(
    nickname = this.nickname,
    profileImg = this.profileImg,
    totalDiaries = this.totalDiaries,
    streak = this.streak
)