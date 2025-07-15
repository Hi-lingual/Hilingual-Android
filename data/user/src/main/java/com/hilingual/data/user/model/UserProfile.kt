package com.hilingual.data.user.model

import com.hilingual.data.user.dto.request.UserProfileRequestDto

data class UserProfile(
    val profileImg: String,
    val nickname: String
)

internal fun UserProfile.toDto(): UserProfileRequestDto = UserProfileRequestDto(
    profileImg = this.profileImg,
    nickname = this.nickname
)
