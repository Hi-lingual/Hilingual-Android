package com.hilingual.data.feed.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiaryDto(
    @SerialName("diaryId")
    val diaryId: Long,
    @SerialName("sharedDate")
    val sharedDate: Long,
    @SerialName("likeCount")
    val likeCount: Int,
    @SerialName("isLiked")
    val isLiked: Boolean,
    @SerialName("diaryImg")
    val diaryImg: String?,
    @SerialName("originalText")
    val originalText: String
)