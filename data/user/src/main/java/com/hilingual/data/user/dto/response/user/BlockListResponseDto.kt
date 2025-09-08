package com.hilingual.data.user.dto.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BlockListResponseDto(
    @SerialName("blockList")
    val blockList: List<BlockUserDto>
)

@Serializable
data class BlockUserDto(
    @SerialName("userId")
    val userId: Long,
    @SerialName("profileImg")
    val profileImageUrl: String,
    @SerialName("nickname")
    val nickname: String
)
