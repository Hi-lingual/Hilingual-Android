package com.hilingual.data.user.model

import com.hilingual.data.user.dto.reponse.UserLoginInfoResponseDto

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
