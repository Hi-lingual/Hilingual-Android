package com.hilingual.data.diary.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookmarkRequestDto(
    @SerialName("isBookmarked")
    val isBookmarked: Boolean
)
