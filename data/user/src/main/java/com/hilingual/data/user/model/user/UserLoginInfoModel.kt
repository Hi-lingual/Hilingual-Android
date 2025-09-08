package com.hilingual.data.user.model.user

import com.hilingual.data.user.dto.response.user.UserLoginInfoResponseDto

data class UserLoginInfoModel(
    val profileImg: String,
    val nickname: String,
    val provider: String
)

internal fun UserLoginInfoResponseDto.toModel() = UserLoginInfoModel(
    profileImg = this.profileImg,
    nickname = this.nickname,
    provider = this.provider
)
