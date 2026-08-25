package com.hilingual.data.widget.datasource

import com.hilingual.data.widget.model.WidgetStreakModel
import com.hilingual.data.widget.model.WidgetTopicModel
import java.time.LocalDate

internal interface WidgetLocalDataSource {
    suspend fun getTopic(date: LocalDate): WidgetTopicModel?

    suspend fun saveTopic(topic: WidgetTopicModel)

    suspend fun getStreak(date: LocalDate): WidgetStreakModel?

    suspend fun saveStreak(streak: WidgetStreakModel)

    suspend fun clear()
}
