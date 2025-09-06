package com.hilingual.data.feed.model

import com.hilingual.data.feed.dto.request.IsLikedRequestDto

data class IsLikedModel(
    val isLiked: Boolean
)

internal fun IsLikedModel.toDto() = IsLikedRequestDto(
    isLiked = this.isLiked
)