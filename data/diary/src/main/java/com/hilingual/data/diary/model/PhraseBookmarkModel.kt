package com.hilingual.data.diary.model

import com.hilingual.data.diary.dto.request.BookmarkRequestDto

data class PhraseBookmarkModel(
    val isBookmarked: Boolean
)

internal fun PhraseBookmarkModel.toDto() = BookmarkRequestDto(
    isBookmarked = this.isBookmarked
)
