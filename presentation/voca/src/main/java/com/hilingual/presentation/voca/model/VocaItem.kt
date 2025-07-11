package com.hilingual.presentation.voca.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class  VocaItem(
    val phraseId: Int,
    val phrase: String,
    val phraseType: ImmutableList<String>,
    val isBookmarked: Boolean = true
)