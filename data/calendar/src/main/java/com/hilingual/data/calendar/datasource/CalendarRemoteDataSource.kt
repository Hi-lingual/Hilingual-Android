package com.hilingual.data.calendar.datasource

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.calendar.dto.reponse.CalendarResponseDto

interface CalendarRemoteDataSource {
    suspend fun getCalendar(
        year: Int,
        month: Int
    ): BaseResponse<CalendarResponseDto>
}
