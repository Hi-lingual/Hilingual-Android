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

val SYSTEM_ZONE_ID: ZoneId = ZoneId.systemDefault()

object DateFormatters {
    val KOREAN_FULL_DATE: DateTimeFormatter = DateTimeFormatter.ofPattern("M월 d일 EEEE", Locale.KOREA)

    val KOREAN_SHORT_DATE: DateTimeFormatter = DateTimeFormatter.ofPattern("M월 d일", Locale.KOREA)

    val FULL_DOT_DATE: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

    val SHORT_YEAR_DOT_DATE: DateTimeFormatter = DateTimeFormatter.ofPattern("yy.MM.dd")

    val ISO_DATE: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
}

/**
 * ISO 8601(UTC) 형식의 문자열을 Instant로 변환합니다.
 * @return 파싱된 Instant (예: "2026-04-12T14:30:00Z" → Instant)
 */
fun String.toUtcInstant(): Instant = Instant.parse(this)

/**
 * "yyyy-MM-dd" 형식의 문자열을 LocalDate로 변환합니다.
 * @return 파싱된 LocalDate (예: "2026-04-12" → LocalDate)
 */
fun String.toLocalDate(): LocalDate = LocalDate.parse(this, DateFormatters.ISO_DATE)

/**
 * Instant를 한국어 짧은 날짜 형식으로 변환합니다.
 * @return "M월 d일" 형식의 문자열 (예: "4월 12일")
 */
fun Instant.toKoreanShortDate(): String =
    atZone(SYSTEM_ZONE_ID).toLocalDate().format(DateFormatters.KOREAN_SHORT_DATE)

/**
 * Instant를 점으로 구분된 전체 날짜 형식으로 변환합니다.
 * @return "yyyy.MM.dd" 형식의 문자열 (예: "2026.04.12")
 */
fun Instant.toFullDotDate(): String =
    atZone(SYSTEM_ZONE_ID).toLocalDate().format(DateFormatters.FULL_DOT_DATE)

/**
 * LocalDate를 연도 두 자리의 점으로 구분된 날짜 형식으로 변환합니다.
 * @return "yy.MM.dd" 형식의 문자열 (예: "26.04.12")
 */
fun LocalDate.toShortYearDotDate(): String = this.format(DateFormatters.SHORT_YEAR_DOT_DATE)

/**
 * LocalDate를 한국어 전체 날짜 형식으로 변환합니다.
 * @return "M월 d일 EEEE" 형식의 문자열 (예: "2월 6일 목요일")
 */
fun LocalDate.toKoreanFullDate(): String = this.format(DateFormatters.KOREAN_FULL_DATE)

/**
 * LocalDate를 ISO 날짜 형식으로 변환합니다.
 * @return "yyyy-MM-dd" 형식의 문자열 (예: "2026-02-06")
 */
fun LocalDate.toIsoDate(): String = this.format(DateFormatters.ISO_DATE)
