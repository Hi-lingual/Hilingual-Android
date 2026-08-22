package com.hilingual.data.widget.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WidgetStreakResponseDto(
    @SerialName("streak")
    val streak: Int,
    @SerialName("recentDays")
    val recentDays: List<WidgetRecentDayResponseDto>,
)

@Serializable
data class WidgetRecentDayResponseDto(
    @SerialName("date")
    val date: String,
    @SerialName("dayOfWeek")
    val dayOfWeek: String,
    @SerialName("isWritten")
    val isWritten: Boolean,
)
