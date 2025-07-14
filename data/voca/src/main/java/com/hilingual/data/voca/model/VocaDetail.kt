package com.hilingual.data.voca.model

import com.hilingual.data.voca.dto.response.VocaDetailResponseDto

data class VocaDetail(
    val phraseId: Long,
    val phrase: String,
    val phraseType: List<String>,
    val explanation: String,
    val createdAt: String,
    val isBookmarked: Boolean
)

fun VocaDetailResponseDto.toModel(): VocaDetail =
    VocaDetail(
        phraseId = this.phraseId,
        phrase = this.phrase,
        phraseType = this.phraseType,
        explanation = this.explanation,
        createdAt = this.createdAt,
        isBookmarked = this.isBookmarked
    )