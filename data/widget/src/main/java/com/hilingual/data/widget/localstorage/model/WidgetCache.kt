/*
 * Copyright 2026 The Hilingual Project
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
package com.hilingual.data.widget.localstorage.model

import com.hilingual.data.widget.model.WidgetRecentDayModel
import com.hilingual.data.widget.model.WidgetStreakModel
import com.hilingual.data.widget.model.WidgetTopicModel
import java.time.LocalDate
import kotlinx.serialization.Serializable

@Serializable
internal data class WidgetTopicCache(
    val date: String,
    val topicEn: String?,
    val isWrittenToday: Boolean?,
)

@Serializable
internal data class WidgetStreakCache(
    val streak: Int,
    val recentDays: List<WidgetRecentDayCache>,
)

@Serializable
internal data class WidgetRecentDayCache(
    val date: String,
    val isWritten: Boolean,
)

internal fun WidgetTopicModel.toCache(): WidgetTopicCache = WidgetTopicCache(
    date = date,
    topicEn = topicEn,
    isWrittenToday = isWrittenToday,
)

internal fun WidgetTopicCache.toModel(): WidgetTopicModel = WidgetTopicModel(
    date = date,
    topicEn = topicEn,
    isWrittenToday = isWrittenToday,
)

internal fun WidgetStreakModel.toCache(): WidgetStreakCache = WidgetStreakCache(
    streak = streak,
    recentDays = recentDays.map { day ->
        WidgetRecentDayCache(
            date = day.date.toString(),
            isWritten = day.isWritten,
        )
    },
)

internal fun WidgetStreakCache.toModel(): WidgetStreakModel = WidgetStreakModel(
    streak = streak,
    recentDays = recentDays.map { day ->
        val date = LocalDate.parse(day.date)
        WidgetRecentDayModel(
            date = date,
            dayOfWeek = date.dayOfWeek,
            isWritten = day.isWritten,
        )
    },
)
