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
package com.hilingual.data.calendar.datasource

import com.hilingual.core.network.model.BaseResponse
import com.hilingual.data.calendar.dto.response.CalendarResponseDto
import com.hilingual.data.calendar.dto.response.DiaryThumbnailResponseDto
import com.hilingual.data.calendar.dto.response.TopicResponseDto

interface CalendarRemoteDataSource {
    suspend fun getCalendar(
        year: Int,
        month: Int
    ): BaseResponse<CalendarResponseDto>

    suspend fun getDiaryThumbnail(
        date: String
    ): BaseResponse<DiaryThumbnailResponseDto>

    suspend fun getTopic(date: String): BaseResponse<TopicResponseDto>
}
