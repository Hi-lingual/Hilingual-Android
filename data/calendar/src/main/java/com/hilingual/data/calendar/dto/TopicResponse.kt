package com.hilingual.data.calendar.dto

import com.hilingual.data.calendar.model.Topic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicResponse(
    @SerialName("topicKor")
    val topicKor: String,
    @SerialName("topicEn")
    val topicEn: String,
    @SerialName("remainingTime")
    val remainingTime: Int
)

internal fun TopicResponse.toModel(): Topic = Topic(
    topicKor = topicKor,
    topicEn = topicEn,
    remainingTime = remainingTime
)