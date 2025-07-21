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
