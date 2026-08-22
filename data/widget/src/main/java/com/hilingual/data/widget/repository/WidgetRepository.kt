package com.hilingual.data.widget.repository

import com.hilingual.data.widget.model.WidgetTopicModel
import java.time.LocalDate

interface WidgetRepository {
    suspend fun getTopic(date: LocalDate): Result<WidgetTopicModel>
}
