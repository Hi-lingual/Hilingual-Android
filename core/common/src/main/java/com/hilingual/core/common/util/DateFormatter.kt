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
package com.hilingual.core.common.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateFormatters {
    val KOREAN_FULL_DATE: DateTimeFormatter = DateTimeFormatter.ofPattern("M월 d일 EEEE", Locale.KOREA)

    val KOREAN_SHORT_DATE: DateTimeFormatter = DateTimeFormatter.ofPattern("M월 d일", Locale.KOREA)

    val FULL_DOT_DATE: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

    val SHORT_YEAR_DOT_DATE: DateTimeFormatter = DateTimeFormatter.ofPattern("yy.MM.dd")

    val ISO_DATE: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
}

fun String.toUtcInstant(): Instant = Instant.parse(this)

fun Instant.toLocalDate(zoneId: ZoneId = ZoneId.systemDefault()): LocalDate = atZone(zoneId).toLocalDate()

/**
 * LocalDate를 한국어 전체 날짜 형식으로 변환합니다.
 * @return "M월 d일 EEEE" 형식의 문자열 (예: "2월 6일 목요일")
 */
fun LocalDate.toKoreanFullDate(): String = this.format(DateFormatters.KOREAN_FULL_DATE)

fun Instant.toKoreanFullDate(zoneId: ZoneId = ZoneId.systemDefault()): String =
    toLocalDate(zoneId).format(DateFormatters.KOREAN_FULL_DATE)

/**
 * LocalDate를 한국어 짧은 날짜 형식으로 변환합니다.
 * @return "M월 d일" 형식의 문자열 (예: "2월 6일")
 */
fun LocalDate.toKoreanShortDate(): String = this.format(DateFormatters.KOREAN_SHORT_DATE)

fun Instant.toKoreanShortDate(zoneId: ZoneId = ZoneId.systemDefault()): String =
    toLocalDate(zoneId).format(DateFormatters.KOREAN_SHORT_DATE)

fun Instant.toFullDotDate(zoneId: ZoneId = ZoneId.systemDefault()): String =
    toLocalDate(zoneId).format(DateFormatters.FULL_DOT_DATE)

fun Instant.toShortYearDotDate(zoneId: ZoneId = ZoneId.systemDefault()): String =
    toLocalDate(zoneId).format(DateFormatters.SHORT_YEAR_DOT_DATE)

/**
 * LocalDate를 ISO 날짜 형식으로 변환합니다.
 * @return "yyyy-MM-dd" 형식의 문자열 (예: "2026-02-06")
 */
fun LocalDate.toIsoDate(): String = this.format(DateFormatters.ISO_DATE)
