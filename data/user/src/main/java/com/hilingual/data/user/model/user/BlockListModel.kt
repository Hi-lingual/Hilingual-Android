package com.hilingual.data.user.model.user

import com.hilingual.data.user.dto.response.user.BlockListResponseDto
import com.hilingual.data.user.dto.response.user.BlockUserDto

data class BlockListModel(
    val blockList: List<BlockUserModel>
)

data class BlockUserModel(
    val userId: Long,
    val profileImageUrl: String,
    val nickname: String
)

internal fun BlockUserDto.toModel(): BlockUserModel =
    BlockUserModel(
        userId = this.userId,
        profileImageUrl = this.profileImageUrl,
        nickname = this.nickname
    )

internal fun BlockListResponseDto.toModel(): BlockListModel =
    BlockListModel(
        blockList = this.blockList.map { it.toModel() }
    )
