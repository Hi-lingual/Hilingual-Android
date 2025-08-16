package com.hilingual.presentation.home.util

import com.hilingual.presentation.home.model.DateUiModel
import java.time.LocalDate

internal fun isDateWritten(date: LocalDate, dateList: List<DateUiModel>): Boolean {
    return dateList.any { LocalDate.parse(it.date) == date }
}

internal fun isDateFuture(date: LocalDate): Boolean {
    return date.isAfter(LocalDate.now())
}

internal fun isDateWritable(date: LocalDate): Boolean {
    val today = LocalDate.now()
    return !date.isAfter(today) && date.isAfter(today.minusDays(2))
}
