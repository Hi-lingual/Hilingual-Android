package com.hilingual.data.calendar.datasouceimpl

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.calendar.datasource.CalendarRemoteDataSource
import com.hilingual.data.calendar.dto.response.CalendarResponseDto
import com.hilingual.data.calendar.service.CalendarService
import jakarta.inject.Inject

import com.hilingual.data.calendar.dto.response.DiaryThumbnailResponseDto

internal class CalendarRemoteDataSourceImpl @Inject constructor(
    private val calendarService: CalendarService
) : CalendarRemoteDataSource {
    override suspend fun getCalendar(year: Int, month: Int): BaseResponse<CalendarResponseDto> =
        calendarService.getCalendar(year = year, month = month)

    override suspend fun getDiaryThumbnail(date: String): BaseResponse<DiaryThumbnailResponseDto> =
        calendarService.getDiaryThumbnail(date = date)
}
