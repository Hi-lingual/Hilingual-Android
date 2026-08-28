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
