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

fun formatSharedDate(sharedMinutes: Long): String {
    return when {
        sharedMinutes < ONE_MINUTE -> "방금 전"
        sharedMinutes < ONE_HOUR_IN_MINUTES -> "${sharedMinutes}분 전"
        sharedMinutes < ONE_DAY_IN_MINUTES -> "${sharedMinutes / ONE_HOUR_IN_MINUTES}시간 전"
        sharedMinutes < ONE_WEEK_IN_MINUTES -> "${sharedMinutes / ONE_DAY_IN_MINUTES}일 전"
        else -> {
            val sharedTime = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(sharedMinutes)
            val instant = Instant.ofEpochMilli(sharedTime)
            val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            DATE_FORMATTER.format(localDateTime)
        }
    }
}
