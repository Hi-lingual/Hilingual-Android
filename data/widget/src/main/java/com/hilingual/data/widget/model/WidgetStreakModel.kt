package com.hilingual.data.widget.model

import com.hilingual.data.widget.dto.response.WidgetStreakResponseDto
import java.time.DayOfWeek
import java.time.LocalDate

data class WidgetStreakModel(
    val streak: Int,
    val recentDays: List<WidgetRecentDayModel>,
)

data class WidgetRecentDayModel(
    val date: LocalDate,
    val dayOfWeek: DayOfWeek,
    val isWritten: Boolean,
)

internal fun WidgetStreakResponseDto.toModel(): WidgetStreakModel = WidgetStreakModel(
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
