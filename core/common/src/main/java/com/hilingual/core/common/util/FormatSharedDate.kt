package com.hilingual.core.common.util

import androidx.compose.runtime.Composable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun formatSharedDate(sharedDate: Long): String {
    return when {
        sharedDate < 1 -> "방금 전"
        sharedDate < 60 -> "${sharedDate}분 전"
        sharedDate < 1440 -> "${sharedDate / 60}시간 전"
        sharedDate < 10080 -> "${sharedDate / 1440}일 전"
        else -> {
            val currentTime = System.currentTimeMillis()
            val sharedTime = currentTime - TimeUnit.MINUTES.toMillis(sharedDate)
            val formatter = DateTimeFormatter.ofPattern("M월 d일", Locale.KOREA)
            val instant = Instant.ofEpochMilli(sharedTime)
            val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            formatter.format(localDateTime)
        }
    }
}
