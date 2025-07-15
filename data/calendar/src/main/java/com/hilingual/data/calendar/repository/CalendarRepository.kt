package com.hilingual.data.calendar.repository

import com.hilingual.data.calendar.model.CalendarModel
import com.hilingual.data.calendar.model.DiaryThumbnailModel
import com.hilingual.data.calendar.model.TopicModel

interface CalendarRepository {
    suspend fun getCalendar(
        year: Int,
        month: Int
    ): Result<CalendarModel>

    suspend fun getDiaryThumbnail(
        date: String
    ): Result<DiaryThumbnailModel>

    suspend fun getTopic(date: String): Result<TopicModel>
}
