package com.hilingual.data.calendar.dto.reponse

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

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
