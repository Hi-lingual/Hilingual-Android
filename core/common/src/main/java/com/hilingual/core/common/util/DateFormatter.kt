package com.hilingual.core.common.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateFormatters {
    val KOREAN_FULL_DATE: DateTimeFormatter = DateTimeFormatter.ofPattern("M월 d일 EEEE", Locale.KOREAN)

    val KOREAN_SHORT_DATE: DateTimeFormatter = DateTimeFormatter.ofPattern("M월 d일", Locale.KOREA)

    val ISO_DATE: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
}

fun LocalDate.toKoreanFullDate(): String = this.format(DateFormatters.KOREAN_FULL_DATE)

fun LocalDate.toKoreanShortDate(): String = this.format(DateFormatters.KOREAN_SHORT_DATE)

fun LocalDate.toIsoDate(): String = this.format(DateFormatters.ISO_DATE)
