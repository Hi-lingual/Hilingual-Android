package com.hilingual.data.calendar.model

import com.hilingual.data.calendar.dto.reponse.CalendarResponseDto
import com.hilingual.data.calendar.dto.reponse.DateDto

data class CalendarModel(
    val dateList: List<DateModel>
)

data class DateModel(
    val date: String
)

fun CalendarResponseDto.toModel() = CalendarModel(
    dateList = this.dateList.map { it.toModel() }
)

fun DateDto.toModel() = DateModel(
    date = this.date
)
