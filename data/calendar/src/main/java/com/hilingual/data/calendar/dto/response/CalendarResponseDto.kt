package com.hilingual.data.calendar.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CalendarResponseDto(
    @SerialName("dateList")
    val dateList: List<DateDto>
)

@Serializable
data class DateDto(
    @SerialName("date")
    val date: String
)
