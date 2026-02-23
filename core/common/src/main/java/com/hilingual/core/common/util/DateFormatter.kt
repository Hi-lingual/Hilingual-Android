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

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateFormatters {
    val KOREAN_FULL_DATE: DateTimeFormatter = DateTimeFormatter.ofPattern("M월 d일 EEEE", Locale.KOREA)

    val KOREAN_SHORT_DATE: DateTimeFormatter = DateTimeFormatter.ofPattern("M월 d일", Locale.KOREA)

    val ISO_DATE: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
}

/**
 * LocalDate를 한국어 전체 날짜 형식으로 변환합니다.
 * @return "M월 d일 EEEE" 형식의 문자열 (예: "2월 6일 목요일")
 */
fun LocalDate.toKoreanFullDate(): String = this.format(DateFormatters.KOREAN_FULL_DATE)

fun LocalDateTime.toKoreanFullDate(): String = this.format(DateFormatters.KOREAN_FULL_DATE)

/**
 * LocalDate를 한국어 짧은 날짜 형식으로 변환합니다.
 * @return "M월 d일" 형식의 문자열 (예: "2월 6일")
 */
fun LocalDate.toKoreanShortDate(): String = this.format(DateFormatters.KOREAN_SHORT_DATE)

fun LocalDateTime.toKoreanShortDate(): String = this.format(DateFormatters.KOREAN_SHORT_DATE)

/**
 * LocalDate를 ISO 날짜 형식으로 변환합니다.
 * @return "yyyy-MM-dd" 형식의 문자열 (예: "2026-02-06")
 */
fun LocalDate.toIsoDate(): String = this.format(DateFormatters.ISO_DATE)

fun LocalDateTime.toIsoDate(): String = this.format(DateFormatters.ISO_DATE)
