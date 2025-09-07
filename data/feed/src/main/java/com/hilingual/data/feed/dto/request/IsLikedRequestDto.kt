package com.hilingual.data.feed.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IsLikedRequestDto(
    @SerialName("isLiked")
    val isLiked: Boolean
)
