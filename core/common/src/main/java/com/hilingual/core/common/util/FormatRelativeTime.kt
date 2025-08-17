package com.hilingual.core.common.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

private const val ONE_MINUTE = 1L
private const val ONE_HOUR_IN_MINUTES = 60L
private const val ONE_DAY_IN_MINUTES = 1440L
private const val ONE_WEEK_IN_MINUTES = 10080L

private val DATE_FORMATTER = DateTimeFormatter.ofPattern("M월 d일", Locale.KOREA)

fun formatRelativeTime(minutesAgo: Long): String {
    return when {
        minutesAgo < ONE_MINUTE -> "방금 전"
        minutesAgo < ONE_HOUR_IN_MINUTES -> "${minutesAgo}분 전"
        minutesAgo < ONE_DAY_IN_MINUTES -> "${minutesAgo / ONE_HOUR_IN_MINUTES}시간 전"
        minutesAgo < ONE_WEEK_IN_MINUTES -> "${minutesAgo / ONE_DAY_IN_MINUTES}일 전"
        else -> {
            val pastTime = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(minutesAgo)
            val instant = Instant.ofEpochMilli(pastTime)
            val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            DATE_FORMATTER.format(localDateTime)
        }
    }
}
