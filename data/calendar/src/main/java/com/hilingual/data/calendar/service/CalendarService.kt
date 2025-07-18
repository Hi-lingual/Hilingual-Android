package com.hilingual.data.calendar.service

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.calendar.dto.response.CalendarResponseDto
import com.hilingual.data.calendar.dto.response.DiaryThumbnailResponseDto
import com.hilingual.data.calendar.dto.response.TopicResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CalendarService {
    @GET("/api/v1/calendar/month")
    suspend fun getCalendar(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): BaseResponse<CalendarResponseDto>

    @GET("/api/v1/calendar/{date}")
    suspend fun getDiaryThumbnail(
        @Path("date") date: String
    ): BaseResponse<DiaryThumbnailResponseDto>

    @GET("/api/v1/calendar/{date}/topic")
    suspend fun getTopic(
        @Path("date") date: String
    ): BaseResponse<TopicResponseDto>
}
