package com.hilingual.data.voca.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VocaMemorizationRequestDto(
    @SerialName("items")
    val items: List<VocaMemorizationItemDto>,
)

@Serializable
data class VocaMemorizationItemDto(
    @SerialName("phraseId")
    val phraseId: Long,
    @SerialName("isMemorized")
    val isMemorized: Boolean,
)
