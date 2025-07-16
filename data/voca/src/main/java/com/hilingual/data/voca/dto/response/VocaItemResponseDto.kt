package com.hilingual.data.voca.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VocaItemResponseDto(
    @SerialName("phraseId")
    val phraseId: Long,
    @SerialName("phrase")
    val phrase: String,
    @SerialName("phraseType")
    val phraseType: List<String>,
    @SerialName("isBookmarked")
    val isBookmarked: Boolean
)
