package com.hilingual.data.calendar.repositoryimpl

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.calendar.datasource.CalendarRemoteDataSource
import com.hilingual.data.calendar.dto.toModel
import com.hilingual.data.calendar.model.CalendarModel
import com.hilingual.data.calendar.model.DiaryThumbnailModel
import com.hilingual.data.calendar.model.toModel
import com.hilingual.data.calendar.repository.CalendarRepository
import jakarta.inject.Inject

internal class CalendarRepositoryImpl @Inject constructor(
    private val calendarRemoteDataSource: CalendarRemoteDataSource
) : CalendarRepository {
    override suspend fun getCalendar(year: Int, month: Int): Result<CalendarModel> =
        suspendRunCatching {
            calendarRemoteDataSource.getCalendar(year = year, month = month).data!!.toModel()
        }

    override suspend fun getDiaryThumbnail(date: String): Result<DiaryThumbnailModel> =
        suspendRunCatching {
            calendarRemoteDataSource.getDiaryThumbnail(date = date).data!!.toModel()
        }

    override suspend fun getTopic(date: String): Result<com.hilingual.data.calendar.model.Topic> =
        suspendRunCatching {
            calendarRemoteDataSource.getTopic(date = date).data!!.toModel()
        }
}
