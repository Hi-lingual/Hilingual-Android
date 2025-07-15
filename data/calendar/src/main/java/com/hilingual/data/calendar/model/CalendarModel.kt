package com.hilingual.data.calendar.model

import com.hilingual.data.calendar.dto.response.CalendarResponseDto
import com.hilingual.data.calendar.dto.response.DateDto

data class CalendarModel(
    val dateList: List<DateModel>
)

data class DateModel(
    val date: String
)

internal fun CalendarResponseDto.toModel() = CalendarModel(
    dateList = this.dateList.map { it.toModel() }
)

internal fun DateDto.toModel() = DateModel(
    date = this.date
)
