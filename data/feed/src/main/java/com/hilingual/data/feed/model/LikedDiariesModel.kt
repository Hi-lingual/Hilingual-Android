package com.hilingual.data.feed.model

import com.hilingual.data.feed.dto.response.LikedDiariesResponseDto

data class LikedDiariesModel(
    val diaryList: List<FeedItemModel>
)

internal fun LikedDiariesResponseDto.toModel(): LikedDiariesModel = LikedDiariesModel(
    diaryList = this.diaryList.map { it.toModel() }
)
