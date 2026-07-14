package com.hilingual.data.calendar.model

enum class CalendarStatus {
    WRITTEN,
    UNLOCKED,
    RECOVERED,
    UNKNOWN,
    ;

    companion object {
        fun from(raw: String): CalendarStatus = entries.find { it.name == raw } ?: UNKNOWN
    }
}
