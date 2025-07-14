package com.hilingual.data.voca.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VocaListResponseDto(
    @SerialName("count")
    val count: Int,
    @SerialName("wordList")
    val wordList: List<VocaGroupResponseDto>
)