package com.hilingual.data.user.dto.reponse

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponseDto(
    @SerialName("nickname")
    val nickname: String,
    @SerialName("profileImg")
    val profileImg: String,
    @SerialName("streak")
    val streak: Int,
    @SerialName("totalDiaries")
    val totalDiaries: Int
)
