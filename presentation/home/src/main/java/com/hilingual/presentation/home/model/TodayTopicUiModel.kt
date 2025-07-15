package com.hilingual.presentation.home.model

import androidx.compose.runtime.Immutable
import com.hilingual.data.calendar.model.Topic

@Immutable
data class TodayTopicUiModel(
    val topicKo: String,
    val topicEn: String,
    val remainingTime: Int
)

internal fun Topic.toState(): TodayTopicUiModel = TodayTopicUiModel(
    topicKo = topicKor,
    topicEn = topicEn,
    remainingTime = remainingTime
)
