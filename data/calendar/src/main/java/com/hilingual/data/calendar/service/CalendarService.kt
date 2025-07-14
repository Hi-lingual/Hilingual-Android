package com.hilingual.data.calendar.service

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.calendar.dto.reponse.CalendarResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CalendarService {
    @GET("/api/v1/calendar/month")
    suspend fun getCalendar(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): BaseResponse<CalendarResponseDto>
}
