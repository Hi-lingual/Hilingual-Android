package com.hilingual.data.widget.model

import com.hilingual.data.widget.dto.response.WidgetTopicResponseDto

data class WidgetTopicModel(
    val date: String,
    val topicEn: String?,
    val isWrittenToday: Boolean?,
)

internal fun WidgetTopicResponseDto.toModel(): WidgetTopicModel = WidgetTopicModel(
    date = date,
    topicEn = topicEn,
    isWrittenToday = isWrittenToday,
)
