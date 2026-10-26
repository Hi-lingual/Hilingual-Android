package com.hilingual.presentation.notification.setting.model

import java.time.DayOfWeek

internal enum class ReminderDay(val dayOfWeek: DayOfWeek, val label: String) {
    SUN(DayOfWeek.SUNDAY, "일"),
    MON(DayOfWeek.MONDAY, "월"),
    TUE(DayOfWeek.TUESDAY, "화"),
    WED(DayOfWeek.WEDNESDAY, "수"),
    THU(DayOfWeek.THURSDAY, "목"),
    FRI(DayOfWeek.FRIDAY, "금"),
    SAT(DayOfWeek.SATURDAY, "토"),
}

internal fun DayOfWeek.toReminderDay(): ReminderDay =
    ReminderDay.entries.first { it.dayOfWeek == this }
