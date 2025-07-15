package com.hilingual.presentation.diaryfeedback.model

import androidx.compose.runtime.Immutable
import com.hilingual.data.diary.model.DiaryContentModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
internal data class DiaryContentUiModel(
    val originalText: String = "",
    val aiText: String = "",
    val diffRanges: ImmutableList<Pair<Int, Int>> = persistentListOf(),
    val imageUrl: String? = null
)

internal fun DiaryContentModel.toState() = DiaryContentUiModel(
    originalText = this.originalText,
    aiText = this.rewriteText,
    diffRanges = this.diffRanges.map {
        it.diffRange.first to it.diffRange.second
    }.toImmutableList(),
    imageUrl = this.imageUrl
)