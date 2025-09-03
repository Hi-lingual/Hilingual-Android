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
package com.hilingual.data.calendar.service

import com.hilingual.core.network.BaseResponse
import com.hilingual.data.calendar.dto.response.CalendarResponseDto
import com.hilingual.data.calendar.dto.response.DiaryThumbnailResponseDto
import com.hilingual.data.calendar.dto.response.TopicResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CalendarService {
    @GET("/api/v1/home/calendar/month")
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
