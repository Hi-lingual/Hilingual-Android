package com.hilingual.presentation.voca.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class VocaItemDetail(
    val phraseId: Long,
    val phrase: String,
    val phraseType: ImmutableList<String>,
    val explanation: String,
    val createdAt: String,
    val isBookmarked: Boolean = true
)
