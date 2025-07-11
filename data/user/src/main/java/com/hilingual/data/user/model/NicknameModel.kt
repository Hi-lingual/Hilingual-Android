package com.hilingual.data.user.model

import com.hilingual.data.user.dto.reponse.NicknameResponseDto

data class NicknameModel(
    val isAvailable: Boolean
)

fun NicknameResponseDto.toModel() = NicknameModel(
    isAvailable = this.isAvailable
)