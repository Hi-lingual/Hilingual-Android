package com.hilingual.data.calendar.datasouceimpl

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.calendar.datasource.CalendarRemoteDataSource
import com.hilingual.data.calendar.dto.response.CalendarResponseDto
import com.hilingual.data.calendar.dto.response.DiaryThumbnailResponseDto
import com.hilingual.data.calendar.dto.response.TopicResponseDto
import com.hilingual.data.calendar.service.CalendarService
import jakarta.inject.Inject

internal class CalendarRemoteDataSourceImpl @Inject constructor(
    private val calendarService: CalendarService
) : CalendarRemoteDataSource {
    override suspend fun getCalendar(year: Int, month: Int): BaseResponse<CalendarResponseDto> =
        calendarService.getCalendar(year = year, month = month)

    override suspend fun getDiaryThumbnail(date: String): BaseResponse<DiaryThumbnailResponseDto> =
        calendarService.getDiaryThumbnail(date = date)

    override suspend fun getTopic(date: String): BaseResponse<TopicResponseDto> =
        calendarService.getTopic(date = date)
}
