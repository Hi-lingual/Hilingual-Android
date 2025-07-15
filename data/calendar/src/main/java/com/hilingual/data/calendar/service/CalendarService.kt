package com.hilingual.data.calendar.service

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.calendar.dto.response.CalendarResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

import com.hilingual.data.calendar.dto.response.DiaryThumbnailResponseDto
import retrofit2.http.Path

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
    ): BaseResponse<com.hilingual.data.calendar.dto.TopicResponse>
}
