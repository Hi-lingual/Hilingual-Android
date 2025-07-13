package com.hilingual.presentation.voca.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class VocaGroup(
    val group: String,
    val words: ImmutableList<VocaItem>
)
