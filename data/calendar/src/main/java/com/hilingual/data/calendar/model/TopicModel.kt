package com.hilingual.data.calendar.model

import com.hilingual.data.calendar.dto.response.TopicResponseDto

data class TopicModel(
    val topicKor: String,
    val topicEn: String,
    val remainingTime: Int
)

internal fun TopicResponseDto.toModel(): TopicModel = TopicModel(
    topicKor = topicKor,
    topicEn = topicEn,
    remainingTime = remainingTime
)
