package com.hilingual.data.diary.model

import com.hilingual.data.diary.dto.response.DiaryContentResponseDto

data class DiaryContentModel(
    val writtenDate: String,
    val originalText: String,
    val rewriteText: String,
    val diffRanges: List<DiaryContentFeedback>,
    val imageUrl: String?
)

data class DiaryContentFeedback(
    val diffRange: Pair<Int, Int>
)

internal fun DiaryContentResponseDto.toModel() = DiaryContentModel(
    writtenDate = this.date,
    originalText = this.originalText,
    rewriteText = this.rewriteText,
    imageUrl = this.imageUrl,
    diffRanges = this.diffRanges.map {
        DiaryContentFeedback(
            Pair(it.start, it.end)
        )
    }
)
