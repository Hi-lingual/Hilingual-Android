package com.hilingual.data.calendar.datasource

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.calendar.dto.response.CalendarResponseDto

import com.hilingual.data.calendar.dto.response.DiaryThumbnailResponseDto

interface CalendarRemoteDataSource {
    suspend fun getCalendar(
        year: Int,
        month: Int
    ): BaseResponse<CalendarResponseDto>

    suspend fun getDiaryThumbnail(
        date: String
    ): BaseResponse<DiaryThumbnailResponseDto>
}
