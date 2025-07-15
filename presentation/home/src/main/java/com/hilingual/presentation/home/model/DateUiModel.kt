package com.hilingual.presentation.home.model

import androidx.compose.runtime.Immutable
import com.hilingual.data.calendar.model.DateModel

@Immutable
data class DateUiModel(
    val date: String
)

internal fun DateModel.toState() = DateUiModel(
    date = this.date
)
