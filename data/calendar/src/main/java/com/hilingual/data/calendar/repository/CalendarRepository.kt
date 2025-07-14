package com.hilingual.data.calendar.repository

import com.hilingual.data.calendar.model.CalendarModel

interface CalendarRepository {
    suspend fun getCalendar(
        year: Int,
        month: Int
    ): Result<CalendarModel>
}
