package com.hilingual.data.voca.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VocaGroupResponseDto(
    @SerialName("group")
    val group: String,
    @SerialName("words")
    val words: List<VocaItemResponseDto>
)
