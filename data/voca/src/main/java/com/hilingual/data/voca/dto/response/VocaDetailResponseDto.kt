package com.hilingual.data.voca.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VocaDetailResponseDto(
    @SerialName("phraseId")
    val phraseId: Long,
    @SerialName("phrase")
    val phrase: String,
    @SerialName("phraseType")
    val phraseType: List<String>,
    @SerialName("explanation")
    val explanation: String,
    @SerialName("writtenDate")
    val createdAt: String,
    @SerialName("isBookmarked")
    val isBookmarked: Boolean
)