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
            localDateTime.toKoreanShortDate()
        }
    }
}
