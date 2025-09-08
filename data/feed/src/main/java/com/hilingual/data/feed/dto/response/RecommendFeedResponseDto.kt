package com.hilingual.data.feed.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendFeedResponseDto(
    @SerialName("diaryList")
    val diaryList: List<FeedItemDto>
)
