/*
 * Copyright 2025 The Hilingual Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hilingual.data.calendar.repositoryimpl

import com.hilingual.core.common.util.suspendRunCatching
import com.hilingual.data.calendar.datasource.CalendarRemoteDataSource
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

    override suspend fun getTopic(date: String): Result<com.hilingual.data.calendar.model.TopicModel> =
        suspendRunCatching {
            calendarRemoteDataSource.getTopic(date = date).data!!.toModel()
        }
}
