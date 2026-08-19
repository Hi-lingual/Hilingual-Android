package com.hilingual.data.voca.model

import com.hilingual.data.voca.dto.request.VocaMemorizationItemDto
import com.hilingual.data.voca.dto.request.VocaMemorizationRequestDto

data class VocaMemorizationModel(
    val phraseId: Long,
    val isMemorized: Boolean,
)

internal fun List<VocaMemorizationModel>.toDto(): VocaMemorizationRequestDto =
    VocaMemorizationRequestDto(
        items = this.map {
            VocaMemorizationItemDto(
                phraseId = it.phraseId,
                isMemorized = it.isMemorized,
            )
        },
    )
