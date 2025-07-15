package com.hilingual.data.voca.model

import com.hilingual.data.voca.dto.response.VocaGroupResponseDto
import com.hilingual.data.voca.dto.response.VocaItemResponseDto

data class VocaList(
    val group: String,
    val words: List<VocaItem>
)

data class VocaItem(
    val phraseId: Long,
    val phrase: String,
    val phraseType: List<String>,
    val isBookmarked: Boolean
)

internal fun VocaGroupResponseDto.toModel(): VocaList =
    VocaList(
        group = this.group,
        words = this.words.map { it.toModel() }
    )

internal fun VocaItemResponseDto.toModel(): VocaItem =
    VocaItem(
        phraseId = this.phraseId,
        phrase = this.phrase,
        phraseType = this.phraseType,
        isBookmarked = this.isBookmarked
    )